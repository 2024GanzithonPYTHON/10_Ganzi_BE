package com.example.APT.chatGPT;

import com.example.APT.chatGPT.DTO.AddMessageRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startProcess(@RequestBody AddMessageRequestDTO request) {
        try {
            String assistantId = "asst_ToFrgJ4L0y77ETK6F6J31imW";
            String threadId = openAIService.createThread();
            openAIService.addMessage(threadId, request.getContent());
            String messages = openAIService.createRunAndPoll(threadId, assistantId);

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
