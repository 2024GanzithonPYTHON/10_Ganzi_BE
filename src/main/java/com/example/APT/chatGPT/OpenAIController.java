package com.example.APT.chatGPT;

import com.example.APT.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
public class OpenAIController {

    private final OpenAIService openAIService;
    private final CategoryService categoryService; // 관심 카테고리 조회 서비스

    public OpenAIController(OpenAIService openAIService, CategoryService categoryService) {
        this.openAIService = openAIService;
        this.categoryService = categoryService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations() {
        try {
            // 1. JWT에서 사용자 ID 추출
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName(); // JWT에서 추출한 사용자 ID (Long 타입)
            System.out.println("추출한 userId" + userId);

            // 2. 사용자 관심 카테고리 조회
            List<String> interestedCategories = categoryService.getInterestedCategoriesByUserName(userId);

            // 디버깅용 로그
            System.out.println("사용자 관심 카테고리: " + interestedCategories);

            // 3. 비관심사 중 랜덤 선택
            List<String> allCategories = List.of(
                    "종이접기", "숨바꼭질", "블록놀이", "그림그리기", "음악놀이",
                    "자연탐험", "보드게임", "과학실험", "보물찾기", "요리놀이", "별보기", "영화보기"
            );
            List<String> remainingCategories = new ArrayList<>(allCategories);
            remainingCategories.removeAll(interestedCategories);
            String randomCategory = remainingCategories.get(new Random().nextInt(remainingCategories.size()));

            // 4. 추천 요청
            List<Map<String, String>> recommendations = openAIService.fetchRecommendations(interestedCategories, randomCategory);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}