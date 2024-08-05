package com.task.jtask.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRecordDto {
    private final String translatedString;
}
