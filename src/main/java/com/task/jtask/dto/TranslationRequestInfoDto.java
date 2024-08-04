package com.task.jtask.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRequestInfoDto {
    private Long id;
    private String ipAddress;
    private String originalStringToTranslate;
    private String translatedString;
}
