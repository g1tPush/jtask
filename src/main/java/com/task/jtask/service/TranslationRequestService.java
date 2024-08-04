package com.task.jtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.response.TranslationResponse;

public interface TranslationRequestService {
    TranslationResponse translate(TranslationDto translationDto);
}
