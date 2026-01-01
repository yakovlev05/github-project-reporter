package ru.yakovlev05.infr.newissuesreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Component;
import ru.yakovlev05.infr.newissuesreporter.dto.IssueDto;
import ru.yakovlev05.infr.newissuesreporter.dto.NodesConnectorDto;
import ru.yakovlev05.infr.newissuesreporter.dto.RepositoryDto;
import ru.yakovlev05.infr.newissuesreporter.entity.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewIssuesGithubClient {

    private final GraphQlClient newIssuesReporterGraphQlClient;

    private static final String DOCUMENT = """
            {
                repository(owner: "%s", name: "%s") {
                    name
                    issues(first: 20, orderBy: { field: UPDATED_AT, direction: DESC }) {
                        totalCount
                        nodes {
                            title
                            number
                            url
                            createdAt
                            updatedAt
                            assignees(first: 10) {
                                nodes {
                                    login
                                    url
                                }
                                totalCount
                            }
                            author {
                                login
                                url
                            }
                        }
                    }
                }
            }
            """;

    public List<IssueDto> pullIssues(Repository repository) {
        try {
            return Optional.ofNullable(newIssuesReporterGraphQlClient
                            .document(prepareDocument(repository))
                            .retrieveSync("repository")
                            .toEntity(RepositoryDto.class)
                    ).map(RepositoryDto::issues)
                    .map(NodesConnectorDto::nodes)
                    .orElse(List.of());
        } catch (Exception e) {
            log.error("Failed to pull repositories", e);
            return List.of();
        }
    }

    public String prepareDocument(Repository repository) {
        return DOCUMENT.formatted(repository.getOwner(), repository.getName());
    }
}
