package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "user_async_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAsyncTask extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Override
    protected void onPrePersist() {
        if (status == null) {
            status = TaskStatus.RUNNING;
        }
    }
}
