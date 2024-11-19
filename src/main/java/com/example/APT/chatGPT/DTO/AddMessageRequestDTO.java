package com.example.APT.chatGPT.DTO;

import lombok.Data;

@Data
public class AddMessageRequestDTO {
    private String content;
    private String role;
}
