package com.task.jtask.controller.impl;

import com.task.jtask.controller.TranslationController;
import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.dto.TranslationRecordDto;
import com.task.jtask.model.TranslationRecord;
import com.task.jtask.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TranslationControllerImpl implements TranslationController {

    private final TranslationService translationServiceImpl;

    public TranslationControllerImpl(TranslationService translationServiceImpl) {
        this.translationServiceImpl = translationServiceImpl;
    }

    @PostMapping("/translation/request")
    public TranslationRecordDto translate(@Valid @RequestBody TranslationInputDto translationInputDto, HttpServletRequest request, BindingResult result) {
        String clientIpAddress = request.getRemoteAddr();

        log.info("Request received with body: {}", translationInputDto);

        TranslationRecord translationRecord = translationServiceImpl.handleTranslationRequest(
                translationInputDto,
                clientIpAddress
        );

        log.info("Create TranslationRecord: {}", translationRecord);

        return TranslationRecordDto.builder()
                .translatedString(translationRecord.getTranslatedString())
                .build();
    }
}
