package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionAnswerRequest {

    private String answer;
    private String question;
}
