package com.example.APT.s3.dto;
import com.example.APT.entity.Activity;
import com.example.APT.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDto {

    private Long id;
    private String content;
    private String oneLine;

    private String imageUrl;
    private LocalDateTime uploadTime;

    private String place;

    private Long userId; // Member의 ID
    private Long activityId; // Activity의 ID

    // 추가적인 메서드나 변환 메서드가 필요할 경우 여기에 추가할 수 있습니다.
}
