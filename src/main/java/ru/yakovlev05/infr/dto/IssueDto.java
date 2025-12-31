package ru.yakovlev05.infr.dto;

import java.time.Instant;

public record IssueDto(
        String title,
        int number,
        String url,
        Instant createdAt,
        Instant updatedAt,
        NodesConnectorDto<UserDto> nodes,
        UserDto author
) {
}
