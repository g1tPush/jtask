package com.task.jtask.exception;

import org.springframework.http.HttpStatusCode;

public class YandexApiTranslationException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    private final String code;
    public YandexApiTranslationException(String message, HttpStatusCode status, String code) {
        super(message);
        this.httpStatusCode = status;
        this.code = code;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getCode() {
        return code;
    }
}
