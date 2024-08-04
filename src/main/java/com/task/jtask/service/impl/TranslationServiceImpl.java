package com.task.jtask.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.entity.TranslationRequestInfo;
import com.task.jtask.repository.TranslationRepository;
import com.task.jtask.response.TranslationResponse;
import com.task.jtask.service.TranslationRequestService;
import com.task.jtask.service.TranslationService;
import org.springframework.stereotype.Service;

@Service
public class TranslationServiceImpl implements TranslationService {

    TranslationRequestService translationRequestService;

    TranslationRepository translationRepository;

    TranslationServiceImpl(TranslationRepository translationRepository, TranslationRequestService translationRequestService) {
        this.translationRepository = translationRepository;
        this.translationRequestService = translationRequestService;
    }

    public TranslationRequestInfo translate(TranslationDto translationDto, String ipAddress) throws JsonProcessingException {
        TranslationResponse translationResponse = translationRequestService.translate(translationDto);

        String translatedText = translationResponse.getTranslations().get(0).getText();

        TranslationRequestInfo translationRequestInfo = TranslationRequestInfo.builder()
                .ipAddress(ipAddress)
                .originalStringToTranslate(translationDto.getText())
                .translatedString(translatedText)
                .build();

        TranslationRequestInfo savedRequestInfo = translationRepository.saveTranslation(translationRequestInfo);

        return savedRequestInfo;
    }

}
