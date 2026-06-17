package com.aicoach.aisystemdesigncoachbackend.repository;

import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession,Long> {

    @EntityGraph(attributePaths = {"questionsAnswers"})
    @NonNull
    Optional<InterviewSession> findById(@NonNull Long id);
}
