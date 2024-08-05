package com.task.jtask.controller.impl;

import com.task.jtask.controller.TranslationController;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.dto.TranslationRequestInfoDto;
import com.task.jtask.model.TranslationRequestInfo;
import com.task.jtask.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TranslationControllerImpl implements TranslationController {

    private final TranslationService translationServiceImpl;

    public TranslationControllerImpl(TranslationService translationServiceImpl) {
        this.translationServiceImpl = translationServiceImpl;
    }

    @PostMapping("/translation/request")
    public TranslationRequestInfoDto translate(@RequestBody TranslationDto translationDto, HttpServletRequest request) {

        String clientIpAddress = request.getRemoteAddr();

        TranslationRequestInfo translationRequestInfo = translationServiceImpl.handleTranslationRequest(
                translationDto,
                clientIpAddress
        );

        return TranslationRequestInfoDto.builder()
                .translatedString(translationRequestInfo.getTranslatedString())
                .build();
    }
}
