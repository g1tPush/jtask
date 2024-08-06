package com.task.jtask.repository.impl;

import com.task.jtask.model.TranslationRecord;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.repository.TranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@Slf4j
public class TranslationRepositoryImpl implements TranslationRepository {
    private final DataSource dataSource;
    private final static String sqlQuery = "INSERT INTO translations (ip_address, original_string_to_translate, translated_string) VALUES (?, ?, ?)";

    public TranslationRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TranslationRecord saveTranslation(TranslationRecord translationRecord) {
        String ipAddress = translationRecord.getIpAddress();
        String originalStringToTranslate = translationRecord.getOriginalStringToTranslate();
        String translatedString = translationRecord.getTranslatedString();

        log.info("Saving translation: {}", translationRecord);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ipAddress);
            ps.setString(2, originalStringToTranslate);
            ps.setString(3, translatedString);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                log.debug("Creating translation failed, no rows affected.");
                throw new GlobalException("Creating translation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    log.info("Translation saved with ID: {}", generatedId);
                    return TranslationRecord.builder()
                            .id(generatedId)
                            .ipAddress(ipAddress)
                            .originalStringToTranslate(originalStringToTranslate)
                            .translatedString(translatedString)
                            .build();
                } else {
                    log.debug("Creating translation failed, no ID obtained.");
                    throw new GlobalException("Creating translation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            log.debug("Database error occurred while saving translation: {}", e.getMessage());
            throw new GlobalException("Database error: " + e.getMessage());
        }
    }
}