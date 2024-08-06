package com.task.jtask.exception;

public class GlobalException extends RuntimeException {
    private final String code;
    public GlobalException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}