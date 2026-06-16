package com.aicoach.aisystemdesigncoachbackend.controller;

import com.aicoach.aisystemdesigncoachbackend.dto.EvaluationRequest;
import com.aicoach.aisystemdesigncoachbackend.dto.EvaluationResponse;
import com.aicoach.aisystemdesigncoachbackend.dto.EvaluationResult;
import com.aicoach.aisystemdesigncoachbackend.entity.Evaluation;
import com.aicoach.aisystemdesigncoachbackend.service.GeminiService;
import com.aicoach.aisystemdesigncoachbackend.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final InterviewService interviewService;

    @Autowired
    public EvaluationController(InterviewService interviewService){
        this.interviewService=interviewService;
    }

    @PostMapping("/{questionAnswerId}")
    public Evaluation evaluate(@PathVariable Long questionAnswerId){

        return interviewService.evaluateQuestionAnswer(questionAnswerId);
    }

}
