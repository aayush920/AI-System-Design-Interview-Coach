package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResult {
    private Integer score;
    private List<String> strengths;
    private List<String> missingPoints;
    private List<String> nextSteps;
}
