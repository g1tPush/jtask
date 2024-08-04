package com.task.jtask.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.jtask.controller.TranslationController;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.dto.TranslationRequestInfoDto;
import com.task.jtask.entity.TranslationRequestInfo;
import com.task.jtask.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TranslationControllerImpl implements TranslationController {

    TranslationService translationServiceImpl;

    TranslationControllerImpl(TranslationService translationServiceImpl) {
        this.translationServiceImpl = translationServiceImpl;
    }

    @PostMapping("/translate")
    public TranslationRequestInfoDto translate(@RequestBody TranslationDto translationDto, HttpServletRequest request) throws JsonProcessingException {

        String clientIpAddress = request.getRemoteAddr();

        TranslationRequestInfo translationRequestInfo = translationServiceImpl.translate(
                translationDto,
                clientIpAddress
        );

        return TranslationRequestInfoDto.builder()
                .translatedString(translationRequestInfo.getTranslatedString())
                .build();
    }
}
