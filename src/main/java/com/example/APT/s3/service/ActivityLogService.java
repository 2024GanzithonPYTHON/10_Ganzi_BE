package com.example.APT.s3.service;

import com.example.APT.dto.ActivityLogRequest;
import com.example.APT.dto.ActivityLogResponse;
import com.example.APT.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.APT.s3.repository.ActivityLogRepository;
import com.example.APT.entity.ActivityLog;
import com.example.APT.entity.Member;
import com.example.APT.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final MemberRepository memberRepository;

    // Create
    public String createActivityLog(String content, String oneLine, String imageUrl, String place, Member member, Activity activity) {
        log.info("기록 저장 시작");

        ActivityLog activityLog = ActivityLog.builder()
                .content(content)
                .oneLine(oneLine)
                .imageUrl(imageUrl)
                .place(place)
                .member(member)
                .activity(activity)
                .build();

        activityLogRepository.saveAndFlush(activityLog);

        log.info("activityId {}", activityLog.getId());
//        member.getActivityLogs().add(activityLog);

        log.info("activityId {}", activityLog.getId());

        return "기록이 정상적으로 저장되었습니다.";
    }

    public ActivityLogResponse getActivityLog(Long id) {
        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("활동이 존재하지 않습니다."));

        return new ActivityLogResponse(activityLog);
    }

    public List<ActivityLogResponse> getAllActivityLogs(UserDetails userDetails) {
        log.info("리스트 조회 시작");
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        List<ActivityLog> activityLogs = member.getActivityLogs().stream()
                .toList();

        return activityLogs.stream()
                .map(ActivityLogResponse::new)
                .toList();
    }

    // Update
    public Optional<ActivityLog> updateActivityLog(Long id, ActivityLogRequest request, UserDetails userDetails) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        Optional<ActivityLog> optionalActivityLog = activityLogRepository.findById(id);

        if (optionalActivityLog.isPresent()) {
            ActivityLog activityLog = optionalActivityLog.get();
            activityLog.setContent(request.getContent());
            activityLog.setOneLine(request.getOneLine());
            return Optional.of(activityLogRepository.save(activityLog));
        }
        return Optional.empty();
    }

    // Delete
    public String deleteActivityLog(Long id, UserDetails userDetails) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("활동이 존재하지 않습니다."));

        member.getActivityLogs().remove(activityLog);
        activityLogRepository.deleteById(id);

        return "활동 기록 삭제가 정상적으로 실행되었습니다.";
    }
}
