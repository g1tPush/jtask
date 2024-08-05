package com.task.jtask.service;

import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.dto.TranslationResponseDto;

public interface TranslationRequestService {
    TranslationResponseDto translate(TranslationInputDto translationInputDto);
}
