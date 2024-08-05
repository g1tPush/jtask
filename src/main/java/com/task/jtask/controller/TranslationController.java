package com.task.jtask.controller;


import com.task.jtask.dto.TranslationInputDto;
import com.task.jtask.dto.TranslationRecordDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TranslationController {
    TranslationRecordDto translate(TranslationInputDto translationRequestInfoDto, HttpServletRequest request);
}
