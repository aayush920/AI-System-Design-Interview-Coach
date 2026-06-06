package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponseContent {
    private List<GeminiPart> parts;
}
