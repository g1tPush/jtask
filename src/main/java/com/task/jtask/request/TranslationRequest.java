package com.task.jtask.request;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class TranslationRequest {
    private String folderId;
    private List<String> texts;
    private String targetLanguageCode;
    private String sourceLanguageCode;
}
