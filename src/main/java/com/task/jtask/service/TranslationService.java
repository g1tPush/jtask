package com.task.jtask.service;

import com.task.jtask.dto.TranslationDto;
import com.task.jtask.entity.TranslationRequestInfo;

public interface TranslationService {
    TranslationRequestInfo handleTranslationRequest(TranslationDto translationDto, String ipAddress);
}
