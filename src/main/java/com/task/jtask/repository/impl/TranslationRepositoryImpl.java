package com.task.jtask.repository.impl;

import com.task.jtask.entity.TranslationRequestInfo;
import com.task.jtask.exception.GlobalException;
import com.task.jtask.repository.TranslationRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class TranslationRepositoryImpl implements TranslationRepository {
    private final DataSource dataSource;
    private final static String sqlQuery = "INSERT INTO translations (ip_address, original_string_to_translate, translated_string) VALUES (?, ?, ?)";

    public TranslationRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TranslationRequestInfo saveTranslation(TranslationRequestInfo translationRequestInfo) {
        String ipAddress = translationRequestInfo.getIpAddress();
        String originalStringToTranslate = translationRequestInfo.getOriginalStringToTranslate();
        String translatedString = translationRequestInfo.getTranslatedString();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, ipAddress);
            ps.setString(2, originalStringToTranslate);
            ps.setString(3, translatedString);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new GlobalException("Creating translation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);
                    return TranslationRequestInfo.builder()
                            .id(generatedId)
                            .ipAddress(ipAddress)
                            .originalStringToTranslate(originalStringToTranslate)
                            .translatedString(translatedString)
                            .build();
                } else {
                    throw new GlobalException("Creating translation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new GlobalException("Database error: " + e.getMessage());
        }
    }
}