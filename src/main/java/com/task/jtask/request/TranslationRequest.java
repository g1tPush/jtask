package com.task.jtask.request;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class TranslationRequest {
    private final String folderId;
    private final List<String> texts;
    private final String targetLanguageCode;
    private final String sourceLanguageCode;
}
