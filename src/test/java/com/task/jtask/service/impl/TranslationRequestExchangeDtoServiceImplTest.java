package com.task.jtask.service.impl;

import com.task.jtask.config.ApiConfig;
import com.task.jtask.config.RequestFactory;
import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.exception.YandexApiTranslationException;
import com.task.jtask.dto.TranslationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
class TranslationRequestExchangeDtoServiceImplTest {
    @Mock
    private ApiConfig apiConfig;

    @Mock
    private RequestFactory requestFactory;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TranslationRequestServiceImpl translationRequestService;

    @Value("${api.url}")
    private String apiUrl;

    TranslationInputDto getTranslationDto() {
        return TranslationInputDto.builder()
                .text("Hello")
                .sourceLanguageCode("en")
                .targetLanguageCode("es")
                .build();
    }

    @BeforeEach
    void setUp() {
        when(apiConfig.getApiUrl()).thenReturn(apiUrl);
        when(requestFactory.createTranslationRequestEntity(getTranslationDto())).thenReturn(new HttpEntity<>(""));
    }

    @Test
    void translate_Success() {
        TranslationResponseDto.Translation translation = new TranslationResponseDto.Translation("Hola");
        TranslationResponseDto expectedResponse = new TranslationResponseDto(Collections.singletonList(translation));

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(TranslationResponseDto.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        TranslationResponseDto actualResponse = translationRequestService.translate(getTranslationDto());

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
                eq(TranslationResponseDto.class)
        )).thenThrow(clientException);

        YandexApiTranslationException exception = assertThrows(YandexApiTranslationException.class, () -> {
            translationRequestService.translate(getTranslationDto());
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
                eq(TranslationResponseDto.class)
        )).thenThrow(serverException);

        GlobalException exception = assertThrows(GlobalException.class, () -> {
            translationRequestService.translate(getTranslationDto());
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
                eq(TranslationResponseDto.class)
        )).thenThrow(clientException);

        GlobalException exception = assertThrows(GlobalException.class, () -> {
            translationRequestService.translate(getTranslationDto());
        });

        assertEquals("Parsing error", exception.getMessage());
    }
}