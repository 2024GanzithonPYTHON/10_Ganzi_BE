package com.example.APT.chatGPT.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RunsRequestDTO {
    @JsonProperty("assistant_id")
    private String assistantId;
}
