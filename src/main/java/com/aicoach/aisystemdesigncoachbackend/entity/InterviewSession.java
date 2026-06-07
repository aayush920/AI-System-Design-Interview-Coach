package com.aicoach.aisystemdesigncoachbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable=false)
    private String topic;

    @Column(nullable=false)
    private LocalDateTime createdAt;


}
