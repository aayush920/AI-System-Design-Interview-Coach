package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequest {

    private String question;
    private String answer;
    private Long questionAnswerId;
}
