package com.task.jtask.repository.impl;

import com.task.jtask.entity.TranslationRequestInfo;
import com.task.jtask.exceptions.GlobalException;
import com.task.jtask.repository.TranslationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TranslationRepositoryImpl implements TranslationRepository {
    private final JdbcTemplate jdbcTemplate;

    public TranslationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TranslationRequestInfo saveTranslation(TranslationRequestInfo translationRequestInfo) {
        String sql = "INSERT INTO translations (ip_address, original_string_to_translate, translated_string) VALUES (?, ?, ?)";

        String ipAddress = translationRequestInfo.getIpAddress();
        String originalStringToTranslate = translationRequestInfo.getOriginalStringToTranslate();
        String translatedString = translationRequestInfo.getTranslatedString();

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, ipAddress);
                ps.setString(2, originalStringToTranslate);
                ps.setString(3, translatedString);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            throw new GlobalException(e.getMessage());
        }

        Long generatedId = keyHolder.getKey().longValue();

        return TranslationRequestInfo.builder()
                .id(generatedId)
                .ipAddress(ipAddress)
                .originalStringToTranslate(originalStringToTranslate)
                .translatedString(translatedString)
                .build();
    }
}
