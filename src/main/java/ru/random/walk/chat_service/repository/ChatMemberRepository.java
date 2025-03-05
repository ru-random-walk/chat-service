package ru.random.walk.chat_service.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends PagingAndSortingRepository<ChatMemberEntity, ChatMemberEntity.ChatMemberId> {
    @Query(
            nativeQuery = true,
            value = """
                    select m.chat_id, array_agg(m.user_id)
                    from chat.chat_members m
                    where m.chat_id in (
                        select member.chat_id
                        from chat.chat_members member
                        where member.user_id = :userId
                    )
                    group by m.chat_id
                    offset :offset
                    limit :limit
                    """
    )
    Map<UUID, List<UUID>> findAllChatToMembersByUserId(UUID userId, Integer offset, Integer limit);

    Optional<ChatMemberEntity> findAllByChatIdAndUserId(UUID chatId, UUID userId);

    ChatMemberEntity save(ChatMemberEntity chatMember);
}
