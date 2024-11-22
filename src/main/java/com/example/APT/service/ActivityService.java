package com.example.APT.service;

import com.example.APT.dto.ActivityRequest;
import com.example.APT.dto.ActivityResponse;
import com.example.APT.entity.Activity;
import com.example.APT.entity.Category;
import com.example.APT.entity.Wish;
import com.example.APT.entity.Member;
import com.example.APT.repository.ActivityRepository;
import com.example.APT.repository.CategoryRepository;
import com.example.APT.repository.LikeRepository;
import com.example.APT.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;


    public Activity saveActivity(UserDetails userDetails, ActivityRequest request) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리가 존재하지 않습니다."));

        Activity activity = Activity.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .imageURL(request.getImageURL())
                .category(category)
                .build();

        Activity activity1 = activityRepository.save(activity);
        activityRepository.flush();

        return activity1;
    }

    @Transactional
    public String likeActivity(UserDetails userDetails, Activity activity) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        Wish wish = new Wish();

        log.info("memberId {} activityId {}", member.getId(), activity.getId());
        wish.setMemberId(member.getId());
        wish.setActivityId(activity.getId());
        wish.setMember(member);
        likeRepository.save(wish); // 영속화
        member.getLikes().add(wish); // 연관 관계 설정

        return "활동과 찜하기가 성공적으로 실행되었습니다.";
    }


    public ActivityResponse getActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("활동이 존재하지 않습니다."));

        return new ActivityResponse(activity);
    }

    public String deleteLikeActivity(Long activityId, UserDetails userDetails) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("활동이 존재하지 않습니다."));

        Wish like = likeRepository.findByMemberIdAndActivityId(member.getId(), activity.getId())
                .orElseThrow(() -> new RuntimeException("좋아요가 존재하지 않습니다."));

        likeRepository.delete(like);
        activityRepository.delete(activity);

        return "찜과 활동 삭제가 성공적으로 실행되었습니다.";
    }

    public List<ActivityResponse> getLikeActivityList(UserDetails userDetails) {
        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        List<Activity> activityList = member.getLikes().stream()
                .map(wish -> activityRepository.findById(wish.getActivityId()).get())
                .toList();

        return activityList.stream()
                .map(ActivityResponse::new)
                .toList();
    }
}
