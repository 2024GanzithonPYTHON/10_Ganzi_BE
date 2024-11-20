package com.example.APT.controller;

import com.example.APT.dto.MemberLoginRequestDto;
import com.example.APT.dto.MemberLoginResponseDto;
import com.example.APT.dto.MemberSignupRequestDto;
import com.example.APT.dto.MemberSignupResponseDto;
import com.example.APT.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MemberSignupResponseDto> create(@RequestBody @Valid final MemberSignupRequestDto request) {
        return new ResponseEntity<>(userService.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberLoginResponseDto> login(@RequestBody @Valid final MemberLoginRequestDto request) {
        System.out.println("request.getLoginId() = " + request.getLoginId());
        System.out.println("request.getPassword() = " + request.getPassword());
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }
}
