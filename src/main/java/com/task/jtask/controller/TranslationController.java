package com.task.jtask.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.jtask.dto.TranslationDto;
import com.task.jtask.dto.TranslationRequestInfoDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TranslationController {
    TranslationRequestInfoDto translate(TranslationDto translationRequestInfoDto, HttpServletRequest request) throws JsonProcessingException;
}
