package com.aicoach.aisystemdesigncoachbackend.controller;

import com.aicoach.aisystemdesigncoachbackend.dto.EvaluationRequest;
import com.aicoach.aisystemdesigncoachbackend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EvaluationController {

    private final GeminiService geminiService;

    @Autowired
    public EvaluationController(GeminiService geminiService){
        this.geminiService=geminiService;
    }

    @PostMapping("/evaluate")
    public String evaluate(@RequestBody EvaluationRequest request){
        return geminiService.evaluate(
                request.getQuestion(),
                request.getAnswer());
    }

}
