package com.reliaquest.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HttpException extends Exception {
    int status;
    String errorMessage;
    Throwable throwable;
}
