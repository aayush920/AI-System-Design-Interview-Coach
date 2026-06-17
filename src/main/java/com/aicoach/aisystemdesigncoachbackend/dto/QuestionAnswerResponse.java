package com.aicoach.aisystemdesigncoachbackend.dto;

import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerResponse {

    private Long id;
    private String question;
    private String answer;
}
