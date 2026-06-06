package com.aicoach.aisystemdesigncoachbackend.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequest {
    private List<GeminiRequestContent> contents;

}
