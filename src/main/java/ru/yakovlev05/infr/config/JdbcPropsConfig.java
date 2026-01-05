package ru.yakovlev05.infr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
public class JdbcPropsConfig {

    @Bean
    public JdbcProps jdbcProps() {
        return new JdbcProps(
                getEnv("JDBC_URL"),
                getEnv("JDBC_USER"),
                getEnv("JDBC_PASSWORD")
        );
    }

    private String getEnv(String name) {
        return Optional.ofNullable(System.getenv(name))
                .orElseThrow(() -> new RuntimeException("Environment variable " + name + " is not set"));
    }

    public record JdbcProps(
            String url,
            String user,
            String password
    ) {
    }
}
