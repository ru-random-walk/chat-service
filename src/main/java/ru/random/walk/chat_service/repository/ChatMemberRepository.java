package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends PagingAndSortingRepository<ChatMemberEntity, ChatMemberEntity.ChatMemberId> {
    Page<ChatMemberEntity> findAllById_UserId(UUID userId, Pageable pageable);

    Optional<ChatMemberEntity> findById_ChatIdAndId_UserId(UUID chatId, UUID userId);

    ChatMemberEntity save(ChatMemberEntity chatMember1);
}
