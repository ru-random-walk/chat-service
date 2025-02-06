package ru.random.walk.chat_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(ChatMemberEntity.ChatMemberId.class)
@Table(name = "chat_members", schema = "chat")
public class ChatMemberEntity {
    @Id
    @Column(name = "chat_id", nullable = false)
    private UUID chatId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMemberId implements Serializable {
        @Column(name = "chat_id", nullable = false)
        private UUID chatId;

        @Column(name = "user_id", nullable = false)
        private UUID userId;
    }

    @Override
    public String toString() {
        return "ChatMemberEntity{" +
                "chatId=" + chatId +
                ", userId=" + userId +
                '}';
    }
}
