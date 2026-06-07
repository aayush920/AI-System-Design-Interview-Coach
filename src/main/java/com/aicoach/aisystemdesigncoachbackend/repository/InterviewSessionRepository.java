package com.aicoach.aisystemdesigncoachbackend.repository;

import com.aicoach.aisystemdesigncoachbackend.entity.InterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession,Long> {

}
