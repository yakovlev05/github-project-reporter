package ru.yakovlev05.infr.newissuesreporter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.yakovlev05.infr.newissuesreporter.dao.IssueDao;
import ru.yakovlev05.infr.newissuesreporter.dto.IssueDto;
import ru.yakovlev05.infr.newissuesreporter.entity.Issue;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NewIssuesReporterService {

    private final NewIssuesGithubClient githubClient;
    private final IssueDao issueDao;
    private final NewIssuesReporterMessageBuilder newIssuesReporterMessageBuilder;
    private final TelegramClient telegramClient;
    private final Supplier<Long> envTimeWindowToNotify;
    private final Supplier<String> envTelegramChatId;

    public NewIssuesReporterService(
            NewIssuesGithubClient githubClient,
            IssueDao issueDao,
            NewIssuesReporterMessageBuilder newIssuesReporterMessageBuilder,
            TelegramClient telegramClient,
            @Qualifier("envTimeWindowToNotify") Supplier<Long> envTimeWindowToNotify,
            @Qualifier("envTelegramChatId") Supplier<String> envTelegramChatId
    ) {
        this.githubClient = githubClient;
        this.issueDao = issueDao;
        this.newIssuesReporterMessageBuilder = newIssuesReporterMessageBuilder;
        this.telegramClient = telegramClient;
        this.envTimeWindowToNotify = envTimeWindowToNotify;
        this.envTelegramChatId = envTelegramChatId;
    }

    public void reportNewIssues(Repository repository) {
        List<IssueDto> issues = githubClient.pullIssues(repository);
        log.info("Received {} issues from github", issues.size());

        List<IssueDto> filteredIssues = filterIssues(repository, issues);
        log.info("After filtering {} issues", filteredIssues.size());

        if (filteredIssues.isEmpty()) {
            return;
        }

        String message = filteredIssues.stream()
                .map(issue -> newIssuesReporterMessageBuilder.build(repository, issue))
                .collect(Collectors.joining("\n\n\\* \\*  \\*  \\*  \\*  \\*  \\*  \\*  \\*\n\n"));

        // отправка в тг
        boolean isSuccess = sendTgNotification(message);
        if (!isSuccess) {
            return;
        }

        issueDao.saveAll(filteredIssues.stream()
                .map(issue -> mapToEntity(repository, issue))
                .toList()
        );
        log.info("Successfully processed {} issues", filteredIssues.size());
    }

    public List<IssueDto> filterIssues(Repository repository, List<IssueDto> issues) {
        Instant timeWindow = Instant.now().minusSeconds(envTimeWindowToNotify.get());
        List<Integer> alreadyProcessed = issueDao
                .findAllByRepositoryAndReportedAndCreatedAt(repository, true, timeWindow)
                .stream()
                .map(Issue::getNumber)
                .toList();

        return issues.stream()
                .filter(issue -> issue.updatedAt().isAfter(timeWindow))
                .filter(issue -> issue.assignees().totalCount() != 0)
                .filter(issue -> !alreadyProcessed.contains(issue.number()))
                .filter(issue -> !(
                        issue.author().login().equals(issue.assignees().nodes().getFirst().login())
                        && issue.assignees().totalCount() == 1
                ))
                .toList();
    }

    public Issue mapToEntity(Repository repository, IssueDto issueDto) {
        return new Issue()
                .setNumber(issueDto.number())
                .setIsReported(true)
                .setCreatedAt(Instant.now())
                .setRepository(repository);
    }

    private boolean sendTgNotification(String message) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(envTelegramChatId.get())
                    .text(message)
                    .parseMode(ParseMode.MARKDOWNV2)
                    .disableWebPagePreview(true)
                    .build());

            return true;
        } catch (TelegramApiException e) {
            log.atError()
                    .setMessage("Failed to send message: {}")
                    .addArgument(message)
                    .setCause(e)
                    .log();
        }

        return false;
    }
}
