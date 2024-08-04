package com.task.jtask.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRequestInfo {
    private Long id;
    private String ipAddress;
    private String originalStringToTranslate;
    private String translatedString;
}
