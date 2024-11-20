package com.example.APT.chatGPT;

import com.example.APT.chatGPT.DTO.AddMessageRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
@RestController
@RequestMapping("/api/v1")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations() {
        try {
            // 관심사 3개 지정
            List<String> interestedCategories = List.of("종이접기", "음악놀이", "보물찾기");

            // 비관심사 중 랜덤 1개 선택
            List<String> allCategories = List.of(
                    "종이접기", "숨바꼭질", "블록놀이", "그림그리기", "음악놀이",
                    "자연탐험", "보드게임", "과학실험", "보물찾기", "요리놀이", "별보기", "영화보기"
            );
            List<String> remainingCategories = new ArrayList<>(allCategories);
            remainingCategories.removeAll(interestedCategories);
            String randomCategory = remainingCategories.get(new Random().nextInt(remainingCategories.size()));

            // 추천 요청
            List<Map<String, String>> recommendations = openAIService.fetchRecommendations(interestedCategories, randomCategory);

            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
