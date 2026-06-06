package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiResponse {
    private List<GeminiCandidate> candidates;
}
