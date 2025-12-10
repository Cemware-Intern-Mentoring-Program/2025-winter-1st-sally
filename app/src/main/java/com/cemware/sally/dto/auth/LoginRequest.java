package com.cemware.sally.dto.auth;

public record LoginRequest(
        String username,
        String password
) {}
