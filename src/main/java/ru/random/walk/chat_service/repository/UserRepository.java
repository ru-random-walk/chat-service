package ru.random.walk.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.random.walk.chat_service.model.entity.UserEntity;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
