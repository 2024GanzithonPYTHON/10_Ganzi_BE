package com.example.APT.s3.controller;

//import com.amazonaws.services.s3.AmazonS3;

import com.example.APT.dto.ActivityLogRequest;
import com.example.APT.entity.Activity;
import com.example.APT.entity.Member;
import com.example.APT.repository.ActivityRepository;
import com.example.APT.repository.MemberRepository;
import com.example.APT.s3.dto.ActiveLogRequestDto;
import com.example.APT.s3.service.ActivityLogService;
import com.example.APT.s3.util.GetUserByJwt;
import com.example.APT.s3.util.S3Service;
import com.example.APT.s3.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityLogController {
    private final S3Service s3Service;
    private final S3Uploader s3Uploader;
    private final ActivityLogService activityLogService;
    private final MemberRepository memberRepository;
    private final GetUserByJwt getUserByJwt;
    private final ActivityRepository activityRepository;


    @PostMapping("/log")
    public ResponseEntity<String> uploadFile(ActiveLogRequestDto request) {
        try {
            Member member = getUserByJwt.getCurrentMember();

            Activity activity = activityRepository.findById(request.getActivityId()).orElseThrow(() -> new IllegalArgumentException("해당 활동이 존재하지 않습니다."));

            String imageUrl = s3Uploader.uploadFileToS3(request.getImageFile(), "폴더 이름");

            return ResponseEntity.ok(activityLogService.createActivityLog(request.getContent(), request.getOneLine(), imageUrl, request.getPlace(), member, activity));
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("log")
    public ResponseEntity<?> getLogList(@RequestParam(value = "activityLogId") Long id) {
        try {
            return ResponseEntity.ok(activityLogService.getActivityLog(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/logList")
    public ResponseEntity<?> getLogList(UserDetails userDetails) {
        try {
            return ResponseEntity.ok(activityLogService.getAllActivityLogs(userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/log")
    public ResponseEntity<?> deleteLog(@RequestParam(value = "activityLogId") Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(activityLogService.deleteActivityLog(id, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/log")
    public ResponseEntity<?> updateLog(@RequestParam(value = "activityLogId") Long id,
                                       @RequestBody ActivityLogRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(activityLogService.updateActivityLog(id, request, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
