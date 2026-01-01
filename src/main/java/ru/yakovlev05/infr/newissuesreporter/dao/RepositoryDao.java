package ru.yakovlev05.infr.newissuesreporter.dao;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RepositoryDao {

    private final EntityManagerFactory entityManagerFactory;

    public List<Repository> findAllActiveRepositories() {
        return entityManagerFactory.callInTransaction(
                em -> em.createQuery("select r from Repository r where r.isActive", Repository.class)
                        .getResultList()
        );
    }

}
