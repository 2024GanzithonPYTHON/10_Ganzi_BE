package com.example.APT.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRequest {
    private String imageURL;
    private String title;
    private String content;
    private Long categoryId;
}
