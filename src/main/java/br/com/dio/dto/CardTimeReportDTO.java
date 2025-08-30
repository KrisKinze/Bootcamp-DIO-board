package br.com.dio.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CardTimeReportDTO(
    Long cardId,
    String cardTitle,
    OffsetDateTime createdAt,
    OffsetDateTime completedAt,
    Long totalDurationMinutes,
    List<ColumnTimeDTO> columnTimes
) {

    public record ColumnTimeDTO(
        String columnName,
        OffsetDateTime enteredAt,
        OffsetDateTime leftAt,
        Long durationMinutes
    ) {}

}
