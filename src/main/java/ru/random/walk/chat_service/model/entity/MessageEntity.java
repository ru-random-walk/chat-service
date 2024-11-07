package ru.random.walk.chat_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.random.walk.chat_service.converter.MessagePayloadConverter;
import ru.random.walk.chat_service.model.entity.payload.MessagePayload;
import ru.random.walk.chat_service.model.entity.type.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "message", schema = "chat")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Convert(converter = MessagePayloadConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    private MessagePayload payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(nullable = false)
    private boolean markedAsRead;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Override
    public String toString() {
        return "Message{" +
                "sentAt=" + sentAt +
                ", markedAsRead=" + markedAsRead +
                ", type=" + type +
                ", id=" + id +
                '}';
    }
}
