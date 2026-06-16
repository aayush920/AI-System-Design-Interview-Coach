package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.dto.EvaluationResult;
import com.aicoach.aisystemdesigncoachbackend.entity.Evaluation;
import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import com.aicoach.aisystemdesigncoachbackend.repository.EvaluationRepository;
import com.aicoach.aisystemdesigncoachbackend.repository.InterviewSessionRepository;
import com.aicoach.aisystemdesigncoachbackend.repository.QuestionAnswerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final EvaluationRepository evaluationRepository;

    private final GeminiService geminiService;

    public InterviewService(InterviewSessionRepository interviewSessionRepository, QuestionAnswerRepository questionAnswerRepository, EvaluationRepository evaluationRepository, GeminiService geminiService){
        this.interviewSessionRepository = interviewSessionRepository;
        this.questionAnswerRepository=questionAnswerRepository;
        this.evaluationRepository=evaluationRepository;
        this.geminiService = geminiService;
    }

    public InterviewSession createInterview(String topic){
        InterviewSession session= new InterviewSession();

        session.setTopic(topic);
        session.setCreatedAt(LocalDateTime.now());

        return interviewSessionRepository.save(session);
    }

    public QuestionAnswer addQuestionAnswer(Long interviewId, String question, String answer){
        InterviewSession interviewSession=interviewSessionRepository.findById(interviewId).orElseThrow();

        QuestionAnswer questionAnswer= new QuestionAnswer();
        questionAnswer.setAnswer(answer);
        questionAnswer.setQuestion(question);
        questionAnswer.setInterviewSession(interviewSession);

        return questionAnswerRepository.save(questionAnswer);
    }

    public Evaluation evaluateQuestionAnswer(Long questionAnswerId){
        QuestionAnswer questionAnswer=questionAnswerRepository.findById(questionAnswerId).orElseThrow();

        EvaluationResult result= geminiService.evaluate(questionAnswer.getQuestion(), questionAnswer.getAnswer());

        Evaluation evaluation= new Evaluation();

        evaluation.setScore(result.getScore());
        evaluation.setQuestionAnswer(questionAnswer);
        evaluation.setStrengths(String.join("\n", result.getStrengths()));
        evaluation.setMissingPoints(String.join("\n", result.getMissingPoints()));
        evaluation.setNextSteps(String.join("\n", result.getNextSteps()));

        return evaluationRepository.save(evaluation);
    }

}
