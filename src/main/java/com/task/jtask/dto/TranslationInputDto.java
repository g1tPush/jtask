package com.task.jtask.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationInputDto {
    @NotEmpty
    @NotNull
    private final String text;
    @NotEmpty
    @NotNull
    private final String targetLanguageCode;

    @NotEmpty
    @NotNull
    private final String sourceLanguageCode;
}
