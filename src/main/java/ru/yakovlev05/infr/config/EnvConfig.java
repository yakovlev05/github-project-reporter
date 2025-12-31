package ru.yakovlev05.infr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Supplier;

@Configuration
public class EnvConfig {

    @Bean
    public Supplier<String> envGithubTokenSupplier() {
        return () -> Optional.ofNullable(System.getenv("GITHUB_TOKEN"))
                .orElseThrow(() -> new RuntimeException("env GITHUB_TOKEN is not set"));
    }

    @Bean
    public Supplier<Long> envNewIssuesReporterTaskDelaySupplier() {
        return () -> Optional.ofNullable(System.getenv("NEW_ISSUES_REPORTER_TASK_DELAY"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("env NEW_ISSUES_REPORTER_TASK_DELAY is not set"));
    }

}
