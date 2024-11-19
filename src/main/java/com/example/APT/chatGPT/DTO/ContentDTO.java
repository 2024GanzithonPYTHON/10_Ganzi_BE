package com.example.APT.chatGPT.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDTO {
    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private TextDTO text;
}

