package com.task.jtask.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.exceptions.GlobalException;
import com.task.jtask.request.TranslationRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestFactory {

    private final ApiConfig apiConfig;
    private final ObjectMapper objectMapper;

    public RequestFactory(ApiConfig apiConfig, ObjectMapper objectMapper) {
        this.apiConfig = apiConfig;
        this.objectMapper = objectMapper;
    }

    public HttpEntity<String> createTranslationRequestEntity(TranslationDto translationDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Api-Key " + apiConfig.getApiKey());
            headers.setContentType(MediaType.APPLICATION_JSON);

            TranslationRequest translationRequest = new TranslationRequest(
                    apiConfig.getFolderId(),
                    List.of(translationDto.getText()),
                    translationDto.getTargetLanguageCode(),
                    translationDto.getSourceLanguageCode()
            );

            String requestBody = objectMapper.writeValueAsString(translationRequest);

            return new HttpEntity<>(requestBody, headers);
        } catch (Exception e) {
            throw new GlobalException("Parsing error");
        }
    }
}
