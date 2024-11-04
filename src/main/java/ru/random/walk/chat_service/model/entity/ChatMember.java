package ru.random.walk.chat_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "chat_members", schema = "chat")
public class ChatMember {
    @EmbeddedId
    private ChatMemberId id;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMemberId implements Serializable {
        @Column(name = "chat_id", nullable = false)
        private UUID chatId;

        @Column(name = "user_id", nullable = false)
        private UUID userId;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id=" + id +
                '}';
    }
}
