package com.task.jtask.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class TranslationRequestExchangeDto {
    private final String folderId;
    private final List<String> texts;
    private final String targetLanguageCode;
    private final String sourceLanguageCode;
}
