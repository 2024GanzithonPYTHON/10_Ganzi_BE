package com.example.APT.dto;

import com.example.APT.entity.ActivityLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityLogResponse {
    private Long activityLogId;
    private String content;
    private String oneLine;
    private String imageURL;
    private String place;
    private LocalDateTime uploadTime;


    public ActivityLogResponse(ActivityLog log) {
        this.activityLogId = log.getId();
        this.content = log.getContent();
        this.oneLine = log.getOneLine();
        this.imageURL = log.getImageUrl();
        this.place = log.getPlace();
        this.uploadTime = log.getUploadTime();
    }
}
