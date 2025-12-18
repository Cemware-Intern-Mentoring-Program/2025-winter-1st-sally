package com.cemware.sally.dto.common;

public record ErrorResponse(
        String message,   // 에러 설명
        String code       // 에러 코드 (임의로 정의)
) {}
