package ru.yakovlev05.infr.dto;

public record RepositoryDto(
        String name,
        NodesConnectorDto<IssueDto> issues
) {
}
