package com.example.APT.chatGPT.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private List<ContentDTO> content;

    @JsonProperty("created_at")
    private Long createdAt;
}

