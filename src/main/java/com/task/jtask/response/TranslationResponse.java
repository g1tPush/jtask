package com.task.jtask.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationResponse {
    private final List<Translation> translations;

    @JsonCreator
    public TranslationResponse(@JsonProperty("translations") List<Translation> translations) {
        this.translations = translations;
    }

    @Data
    public static class Translation {
        private final String text;

        @JsonCreator
        public Translation(@JsonProperty("text") String text) {
            this.text = text;
        }
    }
}