package com.task.jtask.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationDto {
    private final String text;
    private final String targetLanguageCode;
    private final String sourceLanguageCode;
}
