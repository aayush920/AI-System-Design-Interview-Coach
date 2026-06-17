package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDetailResponse {

    private Long id;
    private String topic;
    private List<QuestionAnswerResponse> questionAnswerResponseList;

}
