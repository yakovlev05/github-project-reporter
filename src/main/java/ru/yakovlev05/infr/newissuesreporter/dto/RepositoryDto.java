package ru.yakovlev05.infr.newissuesreporter.dto;

public record RepositoryDto(
        String name,
        NodesConnectorDto<IssueDto> issues
) {
}
