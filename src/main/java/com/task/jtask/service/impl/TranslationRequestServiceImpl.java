package com.task.jtask.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.config.ApiConfig;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.request.TranslationRequest;
import com.task.jtask.response.TranslationResponse;
import com.task.jtask.service.TranslationRequestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TranslationRequestServiceImpl implements TranslationRequestService {

    private final ApiConfig apiConfig;
    RestTemplate restTemplate = new RestTemplate();

    TranslationRequestServiceImpl(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public TranslationResponse translate(TranslationDto translationDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Api-Key " + apiConfig.getApiKey());

        TranslationRequest translationRequest = new TranslationRequest(
                apiConfig.getFolderId(),
                List.of(translationDto.getText()),
                translationDto.getTargetLanguageCode(),
                translationDto.getSourceLanguageCode()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(translationRequest);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TranslationResponse> response = restTemplate.exchange(
                apiConfig.URL,
                HttpMethod.POST,
                entity,
                TranslationResponse.class
        );

        TranslationResponse translationResponse = response.getBody();

        return translationResponse;
    }
}
