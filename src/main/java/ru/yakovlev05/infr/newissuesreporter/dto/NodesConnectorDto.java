package ru.yakovlev05.infr.newissuesreporter.dto;

import java.util.List;

public record NodesConnectorDto<T>(
        List<T> nodes,
        long totalCount
) {
}
