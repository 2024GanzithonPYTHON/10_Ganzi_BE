package com.example.APT.s3.controller;

//import com.amazonaws.services.s3.AmazonS3;
import com.example.APT.s3.util.S3Service;
import com.example.APT.entity.Member;
import com.example.APT.repository.MemberRepository;
import com.example.APT.s3.ActivityLogService;
import com.example.APT.s3.S3Uploader;
import com.example.APT.s3.dto.ActiveLogRequestDto;
import com.example.APT.s3.util.GetUserByJwt;
import com.example.APT.s3.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityLogController {
    private final S3Service s3Service;
    private final S3Uploader s3Uploader;
    private final ActivityLogService activityLogService;
    private final MemberRepository memberRepository;
    private final GetUserByJwt getUserByJwt;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(ActiveLogRequestDto request) {
        try {
//            Member member = memberRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
            Member member = getUserByJwt.getCurrentMember();

//            <Todo> ActivityRepository에서 객체 꺼내와서 넣어주기
//             Activity activity = activityRepository.findById(request.getActivityId()).orElseThrow(() -> new IllegalArgumentException("해당 활동이 존재하지 않습니다."));

            String imageUrl = s3Uploader.uploadFileToS3(request.getImageFile(), "폴더 이름");
//            activityLogService.createActivityLog(request.getContent(), request.getOneLine(), imageUrl, LocalDateTime.now(), member, <Todo> Activity activity);

            return ResponseEntity.ok("파일 업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }



}
