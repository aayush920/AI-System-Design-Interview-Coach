package com.aicoach.aisystemdesigncoachbackend.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSummaryResponse {

    private Long id;
    private String topic;
}
