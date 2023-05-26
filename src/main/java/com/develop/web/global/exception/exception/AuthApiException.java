package com.develop.web.global.exception.exception;

import com.develop.web.global.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthApiException extends RuntimeException {

    private final ErrorCode errorCode;

}
