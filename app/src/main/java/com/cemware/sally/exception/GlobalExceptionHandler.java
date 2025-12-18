package com.cemware.sally.exception;

import com.cemware.sally.dto.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // 모든 @RestController에 공통 적용
public class GlobalExceptionHandler {

    // 1) IllegalArgumentException 공통 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        ErrorResponse body = new ErrorResponse(
                e.getMessage(),
                "BAD_REQUEST"
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    // 2) UsernameNotFoundException (로그인/인증 관련)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException e) {
        ErrorResponse body = new ErrorResponse(
                e.getMessage(),
                "USER_NOT_FOUND"
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    // 3) 그 외 예외들 전체 처리(마지막 안전망)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // 개발 중에는 로그로 찍어두면 좋음
        e.printStackTrace();

        ErrorResponse body = new ErrorResponse(
                "서버 내부 오류가 발생했습니다.",
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
