package com.example.APT.utils.jwt;

import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

public class NoTokenNeederEndpointParser {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public static boolean parsePath(String path) {
        return Arrays.stream(GlobalConstant.noTokenNeadedAPIs)
                .anyMatch(endpoint -> {
                    return pathMatcher.match(endpoint, path);});
    }
}
