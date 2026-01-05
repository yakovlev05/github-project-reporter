package ru.yakovlev05.infr.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Configuration(proxyBeanMethods = false)
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

    @Bean
    public Boolean isProductionMode() {
        boolean isProduction = Optional.ofNullable(System.getenv("APP_IS_PRODUCTION"))
                .map(Boolean::parseBoolean)
                .orElseThrow(() -> new RuntimeException("env APP_IS_PRODUCTION is not set"));
        log.info("App is running in {} mode", isProduction ? "PRODUCTION" : "DEBUG");
        return isProduction;
    }

}
