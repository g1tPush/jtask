package com.task.jtask.controller.advice;


import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
}
