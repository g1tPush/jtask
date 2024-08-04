package com.task.jtask.repository;

import com.task.jtask.entity.TranslationRequestInfo;

public interface TranslationRepository {
    TranslationRequestInfo saveTranslation(TranslationRequestInfo translationRequestInfo);
}
