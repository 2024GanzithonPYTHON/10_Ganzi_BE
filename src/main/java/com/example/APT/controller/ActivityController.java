package com.example.APT.controller;


import com.example.APT.dto.ActivityRequest;
import com.example.APT.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity")
@AllArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/like")
    public ResponseEntity<?> likeActivity(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody ActivityRequest request) {
        try {
            return ResponseEntity.ok(activityService.saveActivity(userDetails, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/like")
    public ResponseEntity<?> deleteLike(@RequestParam(value = "activityId") Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(activityService.deleteLikeActivity(id, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getActivityDetail(@RequestParam(value = "activityId") Long id) {
        try {
            return ResponseEntity.ok(activityService.getActivity(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/like")
    public ResponseEntity<?> getLikeActivityList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(activityService.getLikeActivityList(userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
 }
