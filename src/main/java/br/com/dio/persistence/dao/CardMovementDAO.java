package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.CardMovementEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardMovementDAO {

    private final Connection connection;

    public CardMovementDAO(Connection connection) {
        this.connection = connection;
    }

    public void insert(final CardMovementEntity entity) throws SQLException {
        var sql = """
                INSERT INTO CARD_MOVEMENTS(card_id, from_column_id, to_column_id, moved_at)
                VALUES (?, ?, ?, ?)
                """;
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, entity.getCardId());
            if (entity.getFromColumnId() != null) {
                preparedStatement.setLong(2, entity.getFromColumnId());
            } else {
                preparedStatement.setNull(2, java.sql.Types.BIGINT);
            }
            preparedStatement.setLong(3, entity.getToColumnId());
            preparedStatement.setTimestamp(4, Timestamp.from(
                entity.getMovedAt() != null ? entity.getMovedAt().toInstant() : OffsetDateTime.now().toInstant()
            ));
            preparedStatement.executeUpdate();
        }
    }

    public List<CardMovementEntity> findByCardId(final Long cardId) throws SQLException {
        var sql = """
                SELECT id, card_id, from_column_id, to_column_id, moved_at
                FROM CARD_MOVEMENTS
                WHERE card_id = ?
                ORDER BY moved_at
                """;
        var movements = new ArrayList<CardMovementEntity>();
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, cardId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var movement = new CardMovementEntity();
                    movement.setId(resultSet.getLong("id"));
                    movement.setCardId(resultSet.getLong("card_id"));
                    movement.setFromColumnId(resultSet.getObject("from_column_id", Long.class));
                    movement.setToColumnId(resultSet.getLong("to_column_id"));
                    movement.setMovedAt(resultSet.getTimestamp("moved_at").toInstant().atOffset(OffsetDateTime.now().getOffset()));
                    movements.add(movement);
                }
            }
        }
        return movements;
    }

}
