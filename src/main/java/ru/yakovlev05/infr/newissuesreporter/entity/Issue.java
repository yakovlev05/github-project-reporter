package ru.yakovlev05.infr.newissuesreporter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Accessors(chain = true)
@Getter
@Setter
@Entity
@Table(name = "issue", uniqueConstraints = @UniqueConstraint(columnNames = {"number", "repository_id"}))
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "is_reported", nullable = false)
    private Boolean isReported;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private Repository repository;
}
