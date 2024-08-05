package com.task.jtask.repository;

import com.task.jtask.model.TranslationRequestInfo;

public interface TranslationRepository {
    TranslationRequestInfo saveTranslation(TranslationRequestInfo translationRequestInfo);
}
