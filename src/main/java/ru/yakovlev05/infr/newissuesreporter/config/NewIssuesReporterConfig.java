package ru.yakovlev05.infr.newissuesreporter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;
import java.util.function.Supplier;

@Configuration
public class NewIssuesReporterConfig {

    @Bean
    public GraphQlClient newIssuesReporterGraphQlClient(
            @Qualifier("envGithubTokenSupplier") Supplier<String> envGithubTokenSupplier
    ) {
        return HttpSyncGraphQlClient.builder(RestClient.create("https://api.github.com/graphql"))
                .headers(header -> header.setBearerAuth(envGithubTokenSupplier.get()))
                .build();
    }

    @Bean
    public Supplier<Long> envTimeWindowToNotify() {
        return () -> Optional.ofNullable(System.getenv("TIME_WINDOW_TO_NOTIFY"))
                .map(Long::parseLong)
                .orElseThrow(() -> new RuntimeException("env TIME_WINDOW_TO_NOTIFY is not set"));
    }

    @Bean
    public Supplier<String> envTelegramBotToken() {
        return () -> Optional.ofNullable(System.getenv("TELEGRAM_BOT_TOKEN"))
                .orElseThrow(() -> new RuntimeException("env TELEGRAM_BOT_TOKEN is not set"));
    }

    @Bean
    public TelegramClient telegramClient(
            @Qualifier("envTelegramBotToken") Supplier<String> envTelegramBotToken
    ) {
        return new OkHttpTelegramClient(envTelegramBotToken.get());
    }

    @Bean
    public Supplier<String> envTelegramChatId() {
        return () -> Optional.ofNullable(System.getenv("TELEGRAM_CHAT_ID"))
                .orElseThrow(() -> new RuntimeException("env TELEGRAM_CHAT_ID is not set"));
    }

}
