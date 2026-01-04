package ru.yakovlev05.infr.common;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.config.JdbcPropsConfig.JdbcProps;

@Component
public class FlywayMigration {

    private final JdbcProps jdbcProps;

    public FlywayMigration(JdbcProps jdbcProps) {
        this.jdbcProps = jdbcProps;
        migrate();
    }

    public void migrate() {
        Flyway flyway = Flyway.configure().dataSource(jdbcProps.url(), jdbcProps.user(), jdbcProps.password()).load();
        flyway.migrate();
    }

}
