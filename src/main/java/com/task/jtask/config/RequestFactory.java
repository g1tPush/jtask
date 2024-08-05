package com.task.jtask.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.dto.TranslationRequestExchangeDto;
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

    public HttpEntity<String> createTranslationRequestEntity(TranslationInputDto translationInputDto) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Api-Key " + apiConfig.getApiKey());
            headers.setContentType(MediaType.APPLICATION_JSON);

            TranslationRequestExchangeDto translationRequestExchangeDto = new TranslationRequestExchangeDto(
                    apiConfig.getFolderId(),
                    List.of(translationInputDto.getText()),
                    translationInputDto.getTargetLanguageCode(),
                    translationInputDto.getSourceLanguageCode()
            );

            String requestBody = objectMapper.writeValueAsString(translationRequestExchangeDto);

            return new HttpEntity<>(requestBody, headers);
        } catch (Exception e) {
            throw new GlobalException("Parsing error");
        }
    }
}
