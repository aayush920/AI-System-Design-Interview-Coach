package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.config.GeminiConfig;
import com.aicoach.aisystemdesigncoachbackend.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
public class GeminiService {

    private final GeminiConfig geminiConfig;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiService(GeminiConfig geminiConfig, ObjectMapper objectMapper){
        this.geminiConfig=geminiConfig;
        this.restClient= RestClient.create();
        this.objectMapper=objectMapper;
    }

    /**
     * This fn takes a question and answer provided by the user, builds a gemini prompt on top of these and returns a structured GeminiResponse to the user
     *
     * @param question - question selected by the user
     * @param answer - answer provided by the user
     * @return - extracted Gemini response for the prompt given
     */
    public EvaluationResult evaluateQuestionAnswer(String question, String answer){

        try{
            String prompt= buildQuestionAnswerPrompt(question,answer);

            GeminiRequest request=
                    new GeminiRequest(
                            List.of(
                                    new GeminiRequestContent(
                                            List.of(
                                                    new GeminiPart(prompt)
                                            )
                                    )
                            )
                    );


            String geminiUrl=
                    geminiConfig.getApiUrl()
                            +"?key="
                            +geminiConfig.getApiKey();

            GeminiResponse response =
                    restClient.post()
                            .uri(geminiUrl)
                            .body(request)
                            .retrieve()
                            .body(GeminiResponse.class);

            validateResponse(response);

            String json=
                    response.getCandidates()
                            .getFirst()
                            .getContent()
                            .getParts()
                            .getFirst()
                            .getText();

            return objectMapper.readValue(json, EvaluationResult.class);

        } catch(Exception e){
            throw new RuntimeException(
                    "Failed to evaluate answer using Gemini",
                    e
            );
        }

    }

    private void validateResponse(GeminiResponse response) {

        if (response == null) {
            throw new RuntimeException("Gemini response is null");
        }

        if (response.getCandidates() == null
                || response.getCandidates().isEmpty()) {
            throw new RuntimeException("No candidates returned by Gemini");
        }

        if (response.getCandidates().getFirst().getContent() == null) {
            throw new RuntimeException("Candidate content is missing");
        }

        if (response.getCandidates().getFirst().getContent().getParts() == null
                || response.getCandidates().getFirst().getContent().getParts().isEmpty()) {
            throw new RuntimeException("Candidate parts are missing");
        }
    }

    private String buildQuestionAnswerPrompt(String question, String answer) {

        return """
            You are a senior system design interviewer.

            Question:
            %s

            Candidate Answer:
            %s

            Evaluate the answer and return only valid JSON.
            No Markdown.
            No explanation.
            Only JSON

            Return:
            {
                "score": 0,
                "strengths": [],
                "missingPoints": [],
                "nextSteps":[]
            }

            Keep the response under 200 words.
            """.formatted(question, answer);
    }

    private String buildGenerateQuestionPrompt(String topic) {
        return """
            Generate exactly one system design interview question
            about the following topic: %s

            Return only the question text.
            Do not include an answer, explanation, or numbering.
            """.formatted(topic);
    }

    public String generateQuestion(String topic) {
        try {
            String prompt = buildGenerateQuestionPrompt(topic);
            GeminiRequest request = new GeminiRequest(
                    List.of(
                            new GeminiRequestContent(
                                    List.of(new GeminiPart(prompt))
                            )
                    )
            );

            String geminiUrl = geminiConfig.getApiUrl()
                    + "?key="
                    + geminiConfig.getApiKey();

            GeminiResponse response = restClient.post()
                    .uri(geminiUrl)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            validateResponse(response);

            String question = response.getCandidates()
                    .getFirst()
                    .getContent()
                    .getParts()
                    .getFirst()
                    .getText();

            if (question == null || question.isBlank()) {
                throw new RuntimeException("Gemini returned an empty question");
            }

            return question.trim();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate question using Gemini", e);
        }
    }

    public String generateFollowUpQuestion(
            String topic,
            String originalQuestion,
            String candidateAnswer,
            EvaluationResult evaluation
    ) {
        try {
            String prompt = buildFollowUpQuestionPrompt(
                    topic,
                    originalQuestion,
                    candidateAnswer,
                    evaluation
            );
            GeminiRequest request = new GeminiRequest(
                    List.of(new GeminiRequestContent(List.of(new GeminiPart(prompt))))
            );

            String geminiUrl = geminiConfig.getApiUrl()
                    + "?key="
                    + geminiConfig.getApiKey();

            GeminiResponse response = restClient.post()
                    .uri(geminiUrl)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            validateResponse(response);

            String question = response.getCandidates()
                    .getFirst()
                    .getContent()
                    .getParts()
                    .getFirst()
                    .getText();

            if (question == null || question.isBlank()) {
                throw new RuntimeException("Gemini returned an empty follow-up question");
            }

            return question.trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate follow-up question using Gemini", e);
        }
    }

    private String buildFollowUpQuestionPrompt(
            String topic,
            String originalQuestion,
            String candidateAnswer,
            EvaluationResult evaluation
    ) {
        return """
                You are a senior system design interviewer conducting a continuous interview.

                Interview topic:
                %s

                Previous question:
                %s

                Candidate's answer:
                %s

                Evaluation score:
                %s

                Strengths:
                %s

                Missing points (prioritize these when choosing what to probe):
                %s

                Suggested next steps (use as context, not as text to repeat):
                %s

                Generate exactly one specific follow-up system design interview question.
                Base it on the candidate's answer and evaluation, focusing primarily on a relevant missing point.
                Use the strengths and score to avoid retesting demonstrated knowledge and to choose appropriate depth.
                Keep it connected to the same interview topic and the previous question.
                Return only the question text. Do not include an answer, feedback, explanation, or numbering.
                """.formatted(
                topic,
                originalQuestion,
                candidateAnswer,
                evaluation.getScore(),
                formatEvaluationPoints(evaluation.getStrengths()),
                formatEvaluationPoints(evaluation.getMissingPoints()),
                formatEvaluationPoints(evaluation.getNextSteps())
        );
    }

    private String formatEvaluationPoints(List<String> points) {
        if (points == null || points.isEmpty()) {
            return "None provided";
        }
        return points.stream()
                .map(point -> "- " + point)
                .collect(java.util.stream.Collectors.joining("\n"));
    }
}
