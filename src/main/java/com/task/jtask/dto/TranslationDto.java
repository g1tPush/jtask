package com.task.jtask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationDto {
    private String text;
    private String targetLanguageCode;
    private String sourceLanguageCode;
}
