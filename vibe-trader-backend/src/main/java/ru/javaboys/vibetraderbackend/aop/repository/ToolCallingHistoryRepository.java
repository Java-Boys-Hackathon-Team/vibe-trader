package ru.javaboys.vibetraderbackend.aop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javaboys.vibetraderbackend.chat.model.ToolCallingHistory;

import java.util.Optional;

@Repository
public interface ToolCallingHistoryRepository extends JpaRepository<ToolCallingHistory, Long> {
    Optional<ToolCallingHistory> findByToolName(String toolName);
}
