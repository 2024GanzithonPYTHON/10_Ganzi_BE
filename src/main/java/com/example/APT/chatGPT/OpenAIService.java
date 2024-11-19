package com.example.APT.chatGPT;


import com.example.APT.chatGPT.DTO.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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

    public String createThread() {
        HttpHeaders headers = createHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createThreadUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Parse JSON response to extract thread ID
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("id").asText(); // Assuming the thread ID is under "id"
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse thread creation response", e);
        }
    }

    public void addMessage(String threadId, String content) {
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

    public String createRunAndPoll(String threadId, String assistantId) {
        HttpHeaders headers = createHeaders();

        // Create the request body with assistant_id
        RunsRequestDTO runsRequestDTO = new RunsRequestDTO();
        runsRequestDTO.setAssistantId(assistantId);

        HttpEntity<RunsRequestDTO> entity = new HttpEntity<>(runsRequestDTO, headers);

        // Create Run
        ResponseEntity<String> runResponse = restTemplate.exchange(
                createRunUrl.replace("{threadId}", threadId), // Replace threadId in the URL
                HttpMethod.POST,
                entity,
                String.class // Change to String for raw JSON debugging
        );

        // Debugging: Log the raw JSON response
        System.out.println("Run creation raw response: " + runResponse.getBody());

        // Parse the JSON response to extract runId
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(runResponse.getBody());

            // Check for "id" or "thread_id" fields
            String runId = jsonNode.has("id") ? jsonNode.get("id").asText() :
                    jsonNode.has("thread_id") ? jsonNode.get("thread_id").asText() : null;

            if (runId == null) {
                throw new RuntimeException("Run creation failed. No run ID returned from API.");
            }

            String status = jsonNode.has("status") ? jsonNode.get("status").asText() : null;

            System.out.println("Run ID: " + runId);
            System.out.println("Initial Run Status: " + status);

            // Polling logic here (unchanged)...
            return pollForRunCompletion(threadId, runId, headers);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse run creation response: " + e.getMessage(), e);
        }
    }

    private String pollForRunCompletion(String threadId, String runId, HttpHeaders headers) {
        String status = "queued";
        int maxRetries = 30;
        int retryCount = 0;

        while (!"completed".equals(status)) {
            if (retryCount++ >= maxRetries) {
                throw new RuntimeException("Polling timed out. Run Status: " + status);
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
            } else {
                throw new RuntimeException("Failed to poll run status. No response from API.");
            }
        }

        // Retrieve and return messages
        return fetchMessageValue(threadId, headers);
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
                            return content.getText().getValue();
                        }
                    }
                }
            }

            throw new RuntimeException("No assistant message found in the response.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse messages response: " + e.getMessage(), e);
        }
    }



    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("OpenAI-Beta", "assistants=v2");
        return headers;
    }
}
