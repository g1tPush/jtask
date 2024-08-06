package com.task.jtask.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.jtask.config.ApiConfig;
import com.task.jtask.config.RequestFactory;
import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.exception.YandexApiTranslationException;
import com.task.jtask.dto.TranslationResponseDto;
import com.task.jtask.service.TranslationRequestService;
import com.task.jtask.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
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

    public TranslationResponseDto translate(TranslationInputDto translationInputDto) {
        try {
            HttpEntity<String> entity = requestFactory.createTranslationRequestEntity(translationInputDto);

            log.info("Start text translation");

            ResponseEntity<TranslationResponseDto> response = restTemplate.exchange(
                    apiConfig.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    TranslationResponseDto.class
            );

            log.info("Translation result: {}", response.getBody());

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.debug("HttpClientErrorException: {} {}", e, translationInputDto);
            String errorResponseBody = e.getResponseBodyAsString();
            String message;
            String code;

            try {
                JsonNode jsonNode = objectMapper.readTree(errorResponseBody);
                message = jsonNode.get("message").asText();
                code = jsonNode.get("code").asText();
            } catch (Exception ex) {
                log.debug("HttpClientErrorException parsing: {} {}", ex, translationInputDto);

                throw new GlobalException("Parsing error", ErrorCode.PARSING_ERROR);
            }

            throw new YandexApiTranslationException(message, e.getStatusCode(), code);
        } catch (HttpServerErrorException e) {
            log.debug("HttpServerErrorException: {} {}", e, translationInputDto);

            throw new GlobalException(e.getMessage(), ErrorCode.SERVER_ERROR);
        } catch (Exception e) {
            log.debug("Unknown error: {} {}", e, translationInputDto);

            throw new GlobalException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }
}
