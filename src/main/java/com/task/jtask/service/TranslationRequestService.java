package com.task.jtask.service;

import com.task.jtask.dto.TranslationDto;
import com.task.jtask.dto.TranslationResponseDto;

public interface TranslationRequestService {
    TranslationResponseDto translate(TranslationDto translationDto);
}
