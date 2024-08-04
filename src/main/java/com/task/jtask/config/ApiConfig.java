package com.task.jtask.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class ApiConfig {

    private final Dotenv dotenv;

    public ApiConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public String getApiKey() {
        return dotenv.get("API_KEY");
    }

    public String getFolderId() {
        return dotenv.get("FOLDER_ID");
    }
    public final String URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";
}
