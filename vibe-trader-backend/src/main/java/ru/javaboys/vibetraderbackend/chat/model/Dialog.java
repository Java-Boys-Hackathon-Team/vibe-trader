package ru.javaboys.vibetraderbackend.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ai_dialogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dialog extends BaseEntity {

    @Column(nullable = false)
    private String title;
}
