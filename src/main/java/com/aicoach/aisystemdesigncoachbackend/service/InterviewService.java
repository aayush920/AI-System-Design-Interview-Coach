package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.entity.QuestionAnswer;
import com.aicoach.aisystemdesigncoachbackend.repository.InterviewSessionRepository;
import com.aicoach.aisystemdesigncoachbackend.repository.QuestionAnswerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    public InterviewService(InterviewSessionRepository interviewSessionRepository, QuestionAnswerRepository questionAnswerRepository){
        this.interviewSessionRepository = interviewSessionRepository;
        this.questionAnswerRepository=questionAnswerRepository;
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
}
