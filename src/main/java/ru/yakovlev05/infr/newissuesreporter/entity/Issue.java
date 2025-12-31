package ru.yakovlev05.infr.newissuesreporter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "issue")
public class Issue {

    @Id
    @Column(name = "number")
    private Integer number;

    @Column(name = "is_reported", nullable = false)
    private Boolean isReported;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private Repository repository;
}
