package com.aicoach.aisystemdesigncoachbackend.dto;

import org.springframework.beans.factory.annotation.Autowired;

public class EvaluationRequest {

    private String question;
    private String answer;

    @Autowired
    public EvaluationRequest(String question, String answer){
        this.question=question;
        this.answer=answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
