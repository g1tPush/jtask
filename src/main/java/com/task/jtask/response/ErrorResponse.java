package com.task.jtask.response;


import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
}
