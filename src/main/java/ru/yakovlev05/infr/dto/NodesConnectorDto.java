package ru.yakovlev05.infr.dto;

import java.util.List;

public record NodesConnectorDto<T>(
        List<T> nodes,
        long totalCount
) {
}
