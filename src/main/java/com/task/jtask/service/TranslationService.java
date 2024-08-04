package com.task.jtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.entity.TranslationRequestInfo;

public interface TranslationService {
    TranslationRequestInfo translate(TranslationDto translationDto, String ipAddress) throws JsonProcessingException;
}
