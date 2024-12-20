package com.example.APT.chatGPT.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RootResponseDTO {
    @JsonProperty("data")
    private List<MessageDTO> data;
}

