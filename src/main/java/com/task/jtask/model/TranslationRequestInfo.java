package com.task.jtask.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRequestInfo {
    private final Long id;
    private final String ipAddress;
    private final String originalStringToTranslate;
    private final String translatedString;
}