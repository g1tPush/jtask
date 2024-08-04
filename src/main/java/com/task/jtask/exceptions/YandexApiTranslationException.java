package com.task.jtask.exceptions;

import org.springframework.http.HttpStatusCode;

public class YandexApiTranslationException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    public YandexApiTranslationException(String message, HttpStatusCode status) {
        super(message);
        this.httpStatusCode = status;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
