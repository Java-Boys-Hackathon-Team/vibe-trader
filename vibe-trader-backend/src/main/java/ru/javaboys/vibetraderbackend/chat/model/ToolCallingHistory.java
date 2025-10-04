package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Stores aggregated history of Spring AI @Tool method invocations.
 * One row per tool (tool_name is unique).
 */
@Entity
@Table(name = "tool_calling_history",
        indexes = {
                @Index(name = "idx_tool_calling_history_tool_name", columnList = "tool_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolCallingHistory extends BaseEntity {

    @Column(name = "tool_name", nullable = false, length = 255, unique = true)
    private String toolName;

    // Duration of the last invocation in seconds
    @Column(name = "duration_sec", nullable = false, precision = 15, scale = 3)
    private BigDecimal durationSec;

    // Aggregated counters
    @Column(name = "total_calls", nullable = false)
    private Long totalCalls;

    @Column(name = "total_success", nullable = false)
    private Long totalSuccess;

    @Column(name = "total_failure", nullable = false)
    private Long totalFailure;

    // Was the last invocation successful
    @Column(name = "success", nullable = false)
    private Boolean success;

    // Last successful result as String (full, not truncated)
    @Lob
    @Column(name = "success_result")
    private String successResult;

    // Accumulated size (bytes, UTF-8) of all successful results over time
    @Column(name = "cumulative_success_bytes", nullable = false)
    private Long cumulativeSuccessBytes;

    // Last error details
    @Column(name = "error_class", length = 255)
    private String errorClass;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Lob
    @Column(name = "error_stacktrace")
    private String errorStackTrace;
}
