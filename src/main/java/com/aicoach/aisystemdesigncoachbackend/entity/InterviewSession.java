package com.aicoach.aisystemdesigncoachbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="interview_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSession {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="interviewSession")
    private List<QuestionAnswer> questionsAnswers;

    @Column(nullable=false)
    private String topic;

    @Column(nullable=false)
    private LocalDateTime createdAt;

}
