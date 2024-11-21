package com.example.APT.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.APT.s3.ActivityLogRepository;
import com.example.APT.entity.ActivityLog;
import com.example.APT.entity.Member;
import com.example.APT.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Autowired
    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    // Create
    public ActivityLog createActivityLog(String content, String oneLine, String imageUrl, LocalDateTime uploadTime, String place,  Member user, Activity activity) {
        ActivityLog activityLog = ActivityLog.builder()
                .content(content)
                .oneLine(oneLine)
                .imageUrl(imageUrl)
                .uploadTime(uploadTime)
                .place(place)
                .user(user)
                .activity(activity)
                .build();
        return activityLogRepository.save(activityLog);
    }

    // Read
    public Optional<ActivityLog> getActivityLogById(Long id) {
        return activityLogRepository.findById(id);
    }

    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll();
    }

    // Update
    public Optional<ActivityLog> updateActivityLog(Long id, String content, String oneLine) {
        Optional<ActivityLog> optionalActivityLog = activityLogRepository.findById(id);
        if (optionalActivityLog.isPresent()) {
            ActivityLog activityLog = optionalActivityLog.get();
            activityLog.setContent(content);
            activityLog.setOneLine(oneLine);
            return Optional.of(activityLogRepository.save(activityLog));
        }
        return Optional.empty();
    }

    // Delete
    public void deleteActivityLog(Long id) {
        activityLogRepository.deleteById(id);
    }
}
