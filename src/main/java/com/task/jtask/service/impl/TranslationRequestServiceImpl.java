package com.task.jtask.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.config.ApiConfig;
import com.task.jtask.config.RequestFactory;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.exceptions.GlobalException;
import com.task.jtask.exceptions.YandexApiTranslationException;
import com.task.jtask.response.TranslationResponse;
import com.task.jtask.service.TranslationRequestService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslationRequestServiceImpl implements TranslationRequestService {

    private final ApiConfig apiConfig;
    private final RequestFactory requestFactory;
    private final RestTemplate restTemplate;

    TranslationRequestServiceImpl(ApiConfig apiConfig, RequestFactory requestFactory, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.requestFactory = requestFactory;
        this.apiConfig = apiConfig;
    }

    public TranslationResponse translate(TranslationDto translationDto) {
        try {
            HttpEntity<String> entity = requestFactory.createTranslationRequestEntity(translationDto);

            ResponseEntity<TranslationResponse> response = restTemplate.exchange(
                    apiConfig.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    TranslationResponse.class
            );

            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String errorResponseBody = e.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            String message;

            try {
                JsonNode jsonNode = objectMapper.readTree(errorResponseBody);
                message = jsonNode.get("message").asText();
            } catch (Exception ex) {
                throw new GlobalException("Parsing error");
            }

            throw new YandexApiTranslationException(message, e.getStatusCode());
        }
    }
}
