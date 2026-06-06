package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeminiRequestContent {
    private List<GeminiPart> parts;
}
