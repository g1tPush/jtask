package com.task.jtask.repository;

import com.task.jtask.model.TranslationRecord;

public interface TranslationRepository {
    TranslationRecord saveTranslation(TranslationRecord translationRecord);
}
