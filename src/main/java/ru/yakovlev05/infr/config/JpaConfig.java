package ru.yakovlev05.infr.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.tool.schema.Action;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yakovlev05.infr.config.JdbcPropsConfig.JdbcProps;
import ru.yakovlev05.infr.newissuesreporter.entity.Issue;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

@Configuration
public class JpaConfig {

    @Bean
    public EntityManagerFactory entityManagerFactory(JdbcProps jdbcProps, Boolean isProductionMode) {
        PersistenceConfiguration configuration = new PersistenceConfiguration("MainContext")
                .property(PersistenceConfiguration.JDBC_URL, jdbcProps.url())
                .property(PersistenceConfiguration.JDBC_USER, jdbcProps.user())
                .property(PersistenceConfiguration.JDBC_PASSWORD, jdbcProps.password())
                .property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, Action.UPDATE);

        if (!isProductionMode) {
            configuration
                    .property(JdbcSettings.SHOW_SQL, true)
                    .property(JdbcSettings.FORMAT_SQL, true)
                    .property(JdbcSettings.HIGHLIGHT_SQL, true);
        }

        return configuration
                .managedClass(Repository.class)
                .managedClass(Issue.class)
                .createEntityManagerFactory();
    }

}
