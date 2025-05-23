package ru.random.walk.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.random.walk.chat_service.model.entity.ChatEntity;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
}
