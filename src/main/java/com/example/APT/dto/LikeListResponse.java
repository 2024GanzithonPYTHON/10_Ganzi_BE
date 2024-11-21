package com.example.APT.dto;

import com.example.APT.entity.Activity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeListResponse {
    private Long activityId;
    private String name;

    public LikeListResponse(Activity activity) {
        this.activityId = activity.getId();
        this.name = activity.getTitle();
    }
}
