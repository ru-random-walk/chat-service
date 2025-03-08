package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends PagingAndSortingRepository<ChatMemberEntity, ChatMemberEntity.ChatMemberId> {
    @Query(
            """
                    select new ru.random.walk.chat_service.model.dto.response.ChatDto(m.chatId, array_agg(m.userId) within group (order by m.userId))
                    from ChatMemberEntity m
                    where m.chatId in (
                        select member.chatId
                        from ChatMemberEntity member
                        where member.userId = :userId
                    )
                    group by m.chatId
                    """
    )
    Page<ChatDto> findAllChatToMembersByUserId(UUID userId, Pageable pageable);

    Optional<ChatMemberEntity> findAllByChatIdAndUserId(UUID chatId, UUID userId);

    ChatMemberEntity save(ChatMemberEntity chatMember);
}
