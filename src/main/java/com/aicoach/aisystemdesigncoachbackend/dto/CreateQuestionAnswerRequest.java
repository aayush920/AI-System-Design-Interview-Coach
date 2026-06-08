package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.Data;

@Data
public class CreateQuestionAnswerRequest {

    private String Answer;
    private String question;
}
