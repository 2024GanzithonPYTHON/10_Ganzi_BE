package com.example.APT.utils.jwt;

public class GlobalConstant {
    public static String[] noTokenNeadedAPIs = {
            "/auth/**",
            "/user/signup",
            "/user/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    };
}
