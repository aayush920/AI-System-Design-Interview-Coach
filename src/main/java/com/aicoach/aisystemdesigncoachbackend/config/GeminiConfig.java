package com.aicoach.aisystemdesigncoachbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public String getApiKey(){
        return this.apiKey;
    }

    public String getApiUrl(){
        return this.apiUrl;
    }

}
