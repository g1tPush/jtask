package com.task.jtask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiConfig {
    @Value("${api.folderid}")
    private String folderId;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private String url;

    public String getFolderId() {
        return folderId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return url;
    }
}
