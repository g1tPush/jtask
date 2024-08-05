package com.task.jtask.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRequestInfoDto {
    private final String translatedString;
}
