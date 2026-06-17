package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationHistoryItem {

    private String question;

    private Integer score;

    private String strengths;

    private String missingPoints;

    private String nextSteps;
}
