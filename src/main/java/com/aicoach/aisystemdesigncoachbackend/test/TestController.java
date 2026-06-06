package com.aicoach.aisystemdesigncoachbackend.test;

import com.aicoach.aisystemdesigncoachbackend.config.GeminiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private GeminiConfig geminiConfig;

    public TestController(GeminiConfig geminiConfig){
        this.geminiConfig=geminiConfig;
    }

    @GetMapping("/health")
    public String health(){
        return "Health check ok";
    }

    @GetMapping("/test-key")
    public String getKey(){
        return geminiConfig.getApiKey();
    }
}
