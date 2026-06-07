package com.aicoach.aisystemdesigncoachbackend.service;

import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import com.aicoach.aisystemdesigncoachbackend.repository.InterviewSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewService {

    private final InterviewSessionRepository repository;

    public InterviewService(InterviewSessionRepository repository){
        this.repository=repository;
    }

    public InterviewSession createInterview(String topic){
        InterviewSession session= new InterviewSession();

        session.setTopic(topic);
        session.setCreatedAt(LocalDateTime.now());

        return repository.save(session);
    }
}
