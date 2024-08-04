package com.task.jtask.dto;

import lombok.Data;

@Data
public class TranslationDto {
    private String text;
    private String targetLanguageCode;
    private String sourceLanguageCode;
}
