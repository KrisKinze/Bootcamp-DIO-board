package br.com.dio.service;

import br.com.dio.dto.CardBlockReportDTO;
import br.com.dio.dto.CardTimeReportDTO;
import br.com.dio.persistence.dao.CardMovementDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    private final Connection connection;

    public ReportService(Connection connection) {
        this.connection = connection;
    }

    public List<CardTimeReportDTO> generateTimeReport(final Long boardId) throws SQLException {
        var sql = """
                SELECT 
                    c.id, c.title, c.created_at,
                    CASE 
                        WHEN bc_final.id IS NOT NULL THEN cm_final.moved_at 
                        ELSE NULL 
                    END as completed_at
                FROM CARDS c
                INNER JOIN BOARDS_COLUMNS bc ON c.board_column_id = bc.id
                LEFT JOIN BOARDS_COLUMNS bc_final ON bc.board_id = bc_final.board_id AND bc_final.kind = 'FINAL'
                LEFT JOIN CARD_MOVEMENTS cm_final ON c.id = cm_final.card_id AND cm_final.to_column_id = bc_final.id
                WHERE bc.board_id = ?
                """;

        var reports = new ArrayList<CardTimeReportDTO>();
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var cardId = resultSet.getLong("id");
                    var title = resultSet.getString("title");
                    var createdAt = resultSet.getTimestamp("created_at").toInstant().atOffset(java.time.OffsetDateTime.now().getOffset());
                    var completedAtTimestamp = resultSet.getTimestamp("completed_at");
                    var completedAt = completedAtTimestamp != null ? 
                        completedAtTimestamp.toInstant().atOffset(java.time.OffsetDateTime.now().getOffset()) : null;

                    // Calcular tempo total em minutos
                    Long totalDurationMinutes = null;
                    if (completedAt != null) {
                        totalDurationMinutes = Duration.between(createdAt, completedAt).toMinutes();
                    }

                    // Buscar detalhes de movimento por coluna
                    var columnTimes = generateColumnTimeDetails(cardId);

                    var report = new CardTimeReportDTO(cardId, title, createdAt, completedAt, totalDurationMinutes, columnTimes);
                    reports.add(report);
                }
            }
        }
        return reports;
    }

    public List<CardBlockReportDTO> generateBlockReport(final Long boardId) throws SQLException {
        var sql = """
                SELECT 
                    c.id, c.title
                FROM CARDS c
                INNER JOIN BOARDS_COLUMNS bc ON c.board_column_id = bc.id
                WHERE bc.board_id = ?
                AND EXISTS (SELECT 1 FROM BLOCKS b WHERE b.card_id = c.id)
                """;

        var reports = new ArrayList<CardBlockReportDTO>();
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var cardId = resultSet.getLong("id");
                    var title = resultSet.getString("title");

                    // Buscar histórico de bloqueios
                    var blocks = generateBlockDetails(cardId);

                    var report = new CardBlockReportDTO(cardId, title, blocks);
                    reports.add(report);
                }
            }
        }
        return reports;
    }

    private List<CardTimeReportDTO.ColumnTimeDTO> generateColumnTimeDetails(final Long cardId) throws SQLException {
        var movementDAO = new CardMovementDAO(connection);
        var movements = movementDAO.findByCardId(cardId);
        
        var columnTimes = new ArrayList<CardTimeReportDTO.ColumnTimeDTO>();
        
        // SQL para buscar nomes das colunas
        var columnNameSql = "SELECT id, name FROM BOARDS_COLUMNS WHERE id = ?";
        
        for (int i = 0; i < movements.size(); i++) {
            var movement = movements.get(i);
            var enteredAt = movement.getMovedAt();
            
            // Determinar quando saiu da coluna (próximo movimento ou ainda está lá)
            var leftAt = i < movements.size() - 1 ? movements.get(i + 1).getMovedAt() : null;
            
            // Buscar nome da coluna
            String columnName = null;
            try (var statement = connection.prepareStatement(columnNameSql)) {
                statement.setLong(1, movement.getToColumnId());
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        columnName = resultSet.getString("name");
                    }
                }
            }
            
            // Calcular duração em minutos
            Long durationMinutes = null;
            if (leftAt != null) {
                durationMinutes = Duration.between(enteredAt, leftAt).toMinutes();
            }
            
            var columnTime = new CardTimeReportDTO.ColumnTimeDTO(columnName, enteredAt, leftAt, durationMinutes);
            columnTimes.add(columnTime);
        }
        
        return columnTimes;
    }

    private List<CardBlockReportDTO.BlockInfoDTO> generateBlockDetails(final Long cardId) throws SQLException {
        var sql = """
                SELECT blocked_at, unblocked_at, block_reason, unblock_reason
                FROM BLOCKS
                WHERE card_id = ?
                ORDER BY blocked_at
                """;

        var blocks = new ArrayList<CardBlockReportDTO.BlockInfoDTO>();
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, cardId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var blockedAt = resultSet.getTimestamp("blocked_at").toInstant().atOffset(java.time.OffsetDateTime.now().getOffset());
                    var unblockedAtTimestamp = resultSet.getTimestamp("unblocked_at");
                    var unblockedAt = unblockedAtTimestamp != null ? 
                        unblockedAtTimestamp.toInstant().atOffset(java.time.OffsetDateTime.now().getOffset()) : null;
                    var blockReason = resultSet.getString("block_reason");
                    var unblockReason = resultSet.getString("unblock_reason");

                    // Calcular duração do bloqueio em minutos
                    Long blockedDurationMinutes = null;
                    if (unblockedAt != null) {
                        blockedDurationMinutes = Duration.between(blockedAt, unblockedAt).toMinutes();
                    }

                    var blockInfo = new CardBlockReportDTO.BlockInfoDTO(
                        blockedAt, unblockedAt, blockReason, unblockReason, blockedDurationMinutes
                    );
                    blocks.add(blockInfo);
                }
            }
        }
        return blocks;
    }

}
