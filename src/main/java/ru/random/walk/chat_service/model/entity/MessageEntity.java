package ru.random.walk.chat_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.random.walk.chat_service.converter.MessagePayloadConverter;
import ru.random.walk.chat_service.model.domain.payload.MessagePayload;

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

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(nullable = false)
    private UUID sender;

    @Column(nullable = false)
    private UUID recipient;

    @Column(nullable = false)
    private Boolean markedAsRead;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime sentAt;
}
