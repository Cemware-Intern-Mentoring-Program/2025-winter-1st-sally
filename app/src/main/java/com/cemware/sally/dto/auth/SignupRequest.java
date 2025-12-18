package com.cemware.sally.dto.auth;

public record SignupRequest(
        String username,
        String password
) {}
