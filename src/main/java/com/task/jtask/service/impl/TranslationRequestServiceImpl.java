package com.task.jtask.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.config.ApiConfig;
import com.task.jtask.config.RequestFactory;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.exception.YandexApiTranslationException;
import com.task.jtask.dto.TranslationResponseDto;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TranslationRequestServiceImpl(ApiConfig apiConfig, RequestFactory requestFactory, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.requestFactory = requestFactory;
        this.apiConfig = apiConfig;
    }

    public TranslationResponseDto translate(TranslationDto translationDto) {
        try {
            HttpEntity<String> entity = requestFactory.createTranslationRequestEntity(translationDto);

            ResponseEntity<TranslationResponseDto> response = restTemplate.exchange(
                    apiConfig.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    TranslationResponseDto.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorResponseBody = e.getResponseBodyAsString();
            String message;

            try {
                JsonNode jsonNode = objectMapper.readTree(errorResponseBody);
                message = jsonNode.get("message").asText();
            } catch (Exception ex) {
                throw new GlobalException("Parsing error");
            }

            throw new YandexApiTranslationException(message, e.getStatusCode());
        } catch (HttpServerErrorException e) {
            throw new GlobalException("Server error");
        } catch (Exception e) {
            throw new GlobalException(e.getMessage());
        }
    }
}
