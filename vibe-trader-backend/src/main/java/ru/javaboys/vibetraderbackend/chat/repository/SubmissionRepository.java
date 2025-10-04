package ru.javaboys.vibetraderbackend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaboys.vibetraderbackend.chat.model.Submission;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByPromtUid(String promtUid);
    List<Submission> findAllByAssistantMessage_Id(Long assistantMessageId);
}
