--liquibase formatted sql
--changeset junior:202408301201
--comment: create card_movements table for tracking card movements between columns

CREATE TABLE CARD_MOVEMENTS(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    from_column_id BIGINT NULL,
    to_column_id BIGINT NOT NULL,
    moved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT cards__card_movements_fk FOREIGN KEY (card_id) REFERENCES CARDS(id) ON DELETE CASCADE,
    CONSTRAINT from_column__card_movements_fk FOREIGN KEY (from_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE CASCADE,
    CONSTRAINT to_column__card_movements_fk FOREIGN KEY (to_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE CARD_MOVEMENTS;
