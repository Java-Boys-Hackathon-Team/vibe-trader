package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Base JPA entity for chat domain models.
 *
 * Best practices applied:
 * - @MappedSuperclass so fields are mapped in child entities without separate table.
 * - createdAt is non-updatable and initialized in @PrePersist.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void prePersistDispatcher() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
        onPrePersist();
    }

    /**
     * Hook for subclasses to customize pre-persist logic without declaring another @PrePersist.
     */
    protected void onPrePersist() {
        // no-op by default
    }
}
