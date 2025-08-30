package br.com.dio.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CardBlockReportDTO(
    Long cardId,
    String cardTitle,
    List<BlockInfoDTO> blocks
) {

    public record BlockInfoDTO(
        OffsetDateTime blockedAt,
        OffsetDateTime unblockedAt,
        String blockReason,
        String unblockReason,
        Long blockedDurationMinutes
    ) {}

}
