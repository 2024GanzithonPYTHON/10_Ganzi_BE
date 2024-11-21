package com.example.APT.dto;

import com.example.APT.entity.Activity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityResponse {

    private Long activityId;
    private String name;
    private String imageURL;
    private String content;
    private String categoryName;

    public ActivityResponse(Activity activity) {
        this.activityId = activity.getId();
        this.name = activity.getTitle();
        this.imageURL = activity.getImageURL();
        this.content = activity.getContent();
        this.categoryName = activity.getCategory().getCategoryName();
    }
}
