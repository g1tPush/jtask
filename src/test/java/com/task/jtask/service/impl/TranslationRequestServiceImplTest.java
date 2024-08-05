package com.task.jtask.service.impl;

import com.task.jtask.config.ApiConfig;
import com.task.jtask.config.RequestFactory;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.exceptions.GlobalException;
import com.task.jtask.exceptions.YandexApiTranslationException;
import com.task.jtask.response.TranslationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationRequestServiceImplTest {
    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RequestFactory requestFactory;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TranslationRequestServiceImpl translationRequestService;
    private TranslationDto translationDto;
    private HttpEntity<String> entity;
    private final String apiUrl = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    @BeforeEach
    void setUp() {
        translationDto = TranslationDto.builder()
                .text("Hello")
                .sourceLanguageCode("en")
                .targetLanguageCode("es")
                .build();

        entity = new HttpEntity<>("");

        when(apiConfig.getApiUrl()).thenReturn(apiUrl);
        when(requestFactory.createTranslationRequestEntity(translationDto)).thenReturn(entity);
    }

    @Test
    void translate_Success() {
        TranslationResponse expectedResponse = new TranslationResponse();
        TranslationResponse.Translation translation = new TranslationResponse.Translation();
        translation.setText("Hola");
        expectedResponse.setTranslations(Collections.singletonList(translation));

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(TranslationResponse.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        TranslationResponse actualResponse = translationRequestService.translate(translationDto);

        assertEquals(expectedResponse, actualResponse);
        assertEquals("Hola", actualResponse.getTranslations().get(0).getText());
    }

    @Test
    void translate_HttpClientErrorException() {
        String message = "Invalid request parameters";

        String errorResponse = "{\"message\": \"%s\"}".formatted(message);
        HttpClientErrorException clientException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Bad Request", null, errorResponse.getBytes(), null);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(TranslationResponse.class)
        )).thenThrow(clientException);

        YandexApiTranslationException exception = assertThrows(YandexApiTranslationException.class, () -> {
            translationRequestService.translate(translationDto);
        });

        assertEquals(message, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatusCode());
    }

    @Test
    void translate_HttpServerErrorException() {
        String message = "Server error";

        HttpServerErrorException serverException = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR, message, null, null, null);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(TranslationResponse.class)
        )).thenThrow(serverException);

        GlobalException exception = assertThrows(GlobalException.class, () -> {
            translationRequestService.translate(translationDto);
        });

        assertEquals(message, exception.getMessage());
    }

    @Test
    void translate_ParsingError() {
        String invalidJsonResponse = "invalid json";
        HttpClientErrorException clientException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Bad Request", null, invalidJsonResponse.getBytes(), null);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(TranslationResponse.class)
        )).thenThrow(clientException);

        GlobalException exception = assertThrows(GlobalException.class, () -> {
            translationRequestService.translate(translationDto);
        });

        assertEquals("Parsing error", exception.getMessage());
    }
}