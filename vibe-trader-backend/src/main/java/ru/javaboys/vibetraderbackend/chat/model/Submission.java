package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Submission entity that stores mapping for API call derived from prompts.
 */
@Entity
@Table(name = "submissions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_submissions_promt_uid", columnNames = {"promt_uid"})
        },
        indexes = {
                @Index(name = "idx_submissions_prompt_id", columnList = "prompt_id"),
                @Index(name = "idx_submissions_assistant_message_id", columnList = "assistant_message_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission extends BaseEntity {

    /**
     * Reference to Prompt entity (FK by prompt_id).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    /**
     * Prompt UID used for idempotent upsert operations. Unique across submissions.
     * Note: We keep a separate FK to Prompt via prompt_id; promt_uid is stored for quick upsert lookups.
     */
    @Column(name = "promt_uid", nullable = false, length = 128)
    @NotBlank
    private String promtUid;

    /**
     * HTTP method type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    @NotNull
    private HttpMethodType type;

    /**
     * Request path and query part only (must start with '/').
     * Examples: "/v1/accounts/{account_id}/orders" or "/v1/instruments/GAZP@MISX/bars?timeframe=TIME_FRAME_D".
     */
    @Column(name = "request", nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @Pattern(regexp = "^/[^\\s?#]*?(?:\\?[^\\s#]*)?$",
            message = "Request must be a URL path starting with '/' and may include a query string")
    private String request;

    /**
     * Link to the produced assistant ChatMessage this submission belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assistant_message_id", nullable = false)
    private ChatMessage assistantMessage;
}
