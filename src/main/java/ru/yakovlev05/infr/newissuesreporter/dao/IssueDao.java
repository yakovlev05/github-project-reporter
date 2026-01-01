package ru.yakovlev05.infr.newissuesreporter.dao;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.entity.Issue;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Component
public class IssueDao {

    private final EntityManagerFactory entityManagerFactory;

    public List<Issue> findAllByRepositoryAndReportedAndCreatedAt(
            Repository repository,
            boolean isReported,
            Instant minCreatedAt
    ) {
        return entityManagerFactory.callInTransaction(em ->
                em.createQuery("""
                                select i from Issue i
                                where i.repository = :repository
                                and i.isReported = :isReported
                                and i.createdAt > :minCreatedAt
                                """, Issue.class)
                        .setParameter("repository", repository)
                        .setParameter("isReported", isReported)
                        .setParameter("minCreatedAt", minCreatedAt)
                        .getResultList()
        );
    }

    public void saveAll(List<Issue> issues) {
        entityManagerFactory.runInTransaction(em -> issues.forEach(em::persist));
    }
}
