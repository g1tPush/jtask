package com.task.jtask.service;

import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.model.TranslationRecord;

public interface TranslationService {
    TranslationRecord handleTranslationRequest(TranslationInputDto translationInputDto, String ipAddress);
}
