package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class EvaluationRequest {

    private String question;
    private String answer;

    @Autowired
    public EvaluationRequest(String question, String answer){
        this.question=question;
        this.answer=answer;
    }
}
