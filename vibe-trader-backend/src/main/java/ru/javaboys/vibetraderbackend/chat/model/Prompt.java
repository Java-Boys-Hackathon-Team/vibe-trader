package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prompts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_prompts_message_uid", columnNames = {"chat_message_id", "uid"})
        },
        indexes = {
                @Index(name = "idx_prompts_chat_message_id", columnList = "chat_message_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prompt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;

    @Column(name = "uid", nullable = false, length = 128)
    private String uid;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;
}
