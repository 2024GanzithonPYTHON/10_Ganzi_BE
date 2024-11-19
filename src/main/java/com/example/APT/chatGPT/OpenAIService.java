package com.example.APT.chatGPT;

import com.example.APT.chatGPT.DTO.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class OpenAIService {

    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.url.threads-create}")
    private String createThreadUrl;

    @Value("${openai.url.threads-messages-create}")
    private String createMessageUrl;

    @Value("${openai.url.threads-runs-create-and-poll}")
    private String createRunUrl;

    @Value("${openai.url.threads-messages-list}")
    private String listMessagesUrl;


    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, String>> fetchRecommendations(List<String> interestedCategories, String randomCategory) {
        List<CompletableFuture<Map<String, String>>> responses = new ArrayList<>();

        // 관심사 카테고리 요청 처리
        for (String category : interestedCategories) {
            responses.add(fetchResponseWithRetryAndUrl(category));
        }

        // 랜덤 카테고리 요청 처리
        responses.add(fetchResponseWithRetryAndUrl(randomCategory));

        // CompletableFuture의 결과 수집
        return responses.stream()
                .map(CompletableFuture::join) // 각 요청 완료 대기
                .toList();
    }

    private CompletableFuture<Map<String, String>> fetchResponseWithRetryAndUrl(String category) {
        return CompletableFuture.supplyAsync(() -> {
            int maxRetries = 3;
            int attempt = 0;
            while (attempt < maxRetries) {
                try {
                    String threadId = createThread();
                    addMessage(threadId, category);
                    String assistantId = "asst_ToFrgJ4L0y77ETK6F6J31imW";
                    String responseContent = createRunAndPoll(threadId, assistantId);

                    // URL 결정
                    String imageUrl = determineImageUrl(category);

                    // 응답 데이터 분리
                    String[] lines = responseContent.split("\n", 2);
                    String title = lines[0].trim();
                    String content = lines.length > 1 ? lines[1].trim() : "";

                    // 결과 맵 구성
                    Map<String, String> result = new HashMap<>();
                    result.put("title", title); // 응답의 첫 번째 줄
                    result.put("content", content); // 나머지 내용
                    result.put("imageURL", imageUrl); // 이미지 URL

                    return result;
                } catch (Exception e) {
                    attempt++;
                    System.err.println("Error processing category '" + category + "'. Attempt " + attempt);
                    if (attempt >= maxRetries) {
                        System.err.println("Failed to process category '" + category + "' after " + attempt + " attempts.");

                        // 실패 시 기본 응답 구성
                        Map<String, String> fallbackResult = new HashMap<>();
                        fallbackResult.put("title", "Failed to fetch title for: " + category);
                        fallbackResult.put("content", "Failed to fetch response for category: '" + category + "'.");
                        fallbackResult.put("imageURL", determineImageUrl(category)); // 기본 URL 사용
                        return fallbackResult;
                    }
                }
            }

            // 예외 처리 결과
            Map<String, String> errorResult = new HashMap<>();
            errorResult.put("title", "Unexpected error for: " + category);
            errorResult.put("content", "Unexpected error occurred while processing category '" + category + "'");
            errorResult.put("imageURL", determineImageUrl(category)); // 기본 URL 사용
            return errorResult;
        });
    }

    private String determineImageUrl(String category) {
        // 이미지 URL 매핑
        Map<String, String> imageUrls = Map.ofEntries(
                Map.entry("종이접기", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984.jpg"),
                Map.entry("음악놀이", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_04.jpg"),
                Map.entry("보물찾기", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_08.jpg"),
                Map.entry("숨바꼭질", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_01.jpg"),
                Map.entry("블록놀이", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_02.jpg"),
                Map.entry("그림그리기", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_03.jpg"),
                Map.entry("자연탐험", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_05.jpg"),
                Map.entry("보드게임", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_06.jpg"),
                Map.entry("과학실험", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_07.jpg"),
                Map.entry("요리놀이", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_09.jpg"),
                Map.entry("별보기", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_10.jpg"),
                Map.entry("영화보기", "https://2024ganziton.s3.ap-northeast-2.amazonaws.com/%EA%B0%84%EC%A7%80%ED%86%A4%EC%82%AC%EC%A7%84/KakaoTalk_20241119_192210984_11.jpg")
        );

        return imageUrls.getOrDefault(category, "http://example.com/default.jpg");
    }

    public List<Map<String, String>> formatRecommendations(List<String> recommendations) {
        // 이미지 URL 매핑
        Map<String, String> imageUrls = Map.ofEntries(
                Map.entry("종이접기", "http://example.com/origami.jpg"),
                Map.entry("음악놀이", "http://example.com/music.jpg"),
                Map.entry("보물찾기", "http://example.com/treasure.jpg"),
                Map.entry("숨바꼭질", "http://example.com/hide-seek.jpg"),
                Map.entry("블록놀이", "http://example.com/block-play.jpg"),
                Map.entry("그림그리기", "http://example.com/drawing.jpg"),
                Map.entry("자연탐험", "http://example.com/nature-exploration.jpg"),
                Map.entry("보드게임", "http://example.com/board-game.jpg"),
                Map.entry("과학실험", "http://example.com/science-experiment.jpg"),
                Map.entry("요리놀이", "http://example.com/cooking.jpg"),
                Map.entry("별보기", "http://example.com/star-gazing.jpg"),
                Map.entry("영화보기", "http://example.com/movie.jpg")
        );

        // 결과 변환
        List<Map<String, String>> formattedResults = new ArrayList<>();
        for (String recommendation : recommendations) {
            String[] lines = recommendation.split("\n", 2);
            String title = lines[0].trim();
            String content = lines.length > 1 ? lines[1].trim() : "";

            Map<String, String> formatted = new HashMap<>();
            formatted.put("title", title);
            formatted.put("content", content);
            formatted.put("imageURL", imageUrls.getOrDefault(title, "http://example.com/default.jpg"));

            formattedResults.add(formatted);
        }

        return formattedResults;
    }

    private String fetchMessageValue(String threadId, HttpHeaders headers) {
        // Fetch messages from the API
        ResponseEntity<String> messagesResponse = restTemplate.exchange(
                listMessagesUrl.replace("{threadId}", threadId),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class // Fetch raw JSON for debugging
        );

        // Debugging: Print the raw JSON response
        System.out.println("Messages raw response: " + messagesResponse.getBody());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            RootResponseDTO rootResponse = objectMapper.readValue(
                    messagesResponse.getBody(),
                    RootResponseDTO.class
            );

            // Find the first message with the "assistant" role
            for (MessageDTO message : rootResponse.getData()) {
                if ("assistant".equals(message.getRole())) {
                    if (message.getContent() != null && !message.getContent().isEmpty()) {
                        ContentDTO content = message.getContent().get(0);
                        if (content.getText() != null) {
                            return content.getText().getValue(); // Return the value field
                        }
                    }
                }
            }

            throw new RuntimeException("No assistant message found in the response.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse messages response: " + e.getMessage(), e);
        }
    }

    public CompletableFuture<String> fetchResponseWithRetry(String category) {
        return CompletableFuture.supplyAsync(() -> {
            int maxRetries = 3;
            int attempt = 0;
            while (attempt < maxRetries) {
                try {
                    String threadId = createThread();
                    addMessage(threadId, category);
                    String assistantId = "asst_ToFrgJ4L0y77ETK6F6J31imW";
                    return createRunAndPoll(threadId, assistantId); // Fetch value from API
                } catch (Exception e) {
                    attempt++;
                    System.err.println("Error processing category '" + category + "'. Attempt " + attempt);
                    if (attempt >= maxRetries) {
                        System.err.println("Failed to process category '" + category + "' after " + attempt + " attempts.");
                        return "Failed to fetch response for category: '" + category + "'.";
                    }
                }
            }
            return "Unexpected error occurred while processing category '" + category + "'";
        });
    }

    /**
     * OpenAI 스레드 생성
     */
    private String createThread() {
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createThreadUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse thread creation response", e);
        }
    }

    /**
     * OpenAI 메시지 추가
     */
    private void addMessage(String threadId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be null or empty");
        }

        HttpHeaders headers = createHeaders();
        AddMessageRequestDTO requestDTO = new AddMessageRequestDTO();
        requestDTO.setContent(content);
        requestDTO.setRole("user");

        HttpEntity<AddMessageRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        restTemplate.exchange(
                createMessageUrl.replace("{threadId}", threadId),
                HttpMethod.POST,
                entity,
                Void.class
        );
    }


    /**
     * OpenAI Polling 및 결과 가져오기
     */
    private String createRunAndPoll(String threadId, String assistantId) {
        HttpHeaders headers = createHeaders();
        RunsRequestDTO runsRequestDTO = new RunsRequestDTO();
        runsRequestDTO.setAssistantId(assistantId);

        HttpEntity<RunsRequestDTO> entity = new HttpEntity<>(runsRequestDTO, headers);

        ResponseEntity<String> runResponse = restTemplate.exchange(
                createRunUrl.replace("{threadId}", threadId),
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(runResponse.getBody());
            String runId = jsonNode.has("id") ? jsonNode.get("id").asText() : null;

            if (runId == null) {
                throw new RuntimeException("Run creation failed. No run ID returned.");
            }

            pollForRunCompletion(threadId, runId, headers); // Ensure run is completed
            return fetchMessageValue(threadId, headers); // Fetch and return value
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse run creation response", e);
        }
    }


    /**
     * Polling 실행
     */
    private String pollForRunCompletion(String threadId, String runId, HttpHeaders headers) {
        String status = "queued";
        int maxRetries = 30;
        int retryCount = 0;

        while (!"completed".equals(status)) {
            if (retryCount++ >= maxRetries) {
                throw new RuntimeException("Polling timed out. Last Run Status: " + status);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Polling interrupted", e);
            }

            String pollUrl = createRunUrl.replace("{threadId}", threadId) + "/" + runId;
            ResponseEntity<RunResponseDTO> pollResponse = restTemplate.exchange(
                    pollUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    RunResponseDTO.class
            );

            if (pollResponse.getBody() != null) {
                status = pollResponse.getBody().getStatus();
                System.out.println("Polling Run Status: " + status);

                if ("failed".equals(status)) {
                    // 실패 상태인 경우 명확한 로그 출력 및 예외 발생
                    System.err.println("Polling failed for Thread ID: " + threadId + ", Run ID: " + runId);
                    throw new RuntimeException("Run failed during polling. Thread ID: " + threadId + ", Run ID: " + runId);
                }
            } else {
                throw new RuntimeException("Failed to poll run status. No response from API.");
            }
        }

        return "Run completed successfully";
    }

    /**
     * HTTP 헤더 생성
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("OpenAI-Beta", "assistants=v2");
        return headers;
    }
}
