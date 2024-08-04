package com.task.jtask.exceptions;

import org.springframework.http.HttpStatusCode;

public class WrongLanguageCodeException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    public WrongLanguageCodeException(String message, HttpStatusCode status) {
        super(message);
        this.httpStatusCode = status;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
