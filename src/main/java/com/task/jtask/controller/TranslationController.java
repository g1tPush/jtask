package com.task.jtask.controller;


import com.task.jtask.dto.TranslationDto;
import com.task.jtask.dto.TranslationRequestInfoDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TranslationController {
    TranslationRequestInfoDto translate(TranslationDto translationRequestInfoDto, HttpServletRequest request);
}
