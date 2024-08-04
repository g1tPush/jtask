package com.task.jtask.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationResponse {
    private List<Translation> translations;

    @Data
    public static class Translation {
        private String text;
    }
}
