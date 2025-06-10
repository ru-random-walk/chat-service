package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.ChatWithMembersEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMemberEntity, ChatMemberEntity.ChatMemberId> {
    @Query(
            """
                    select new ru.random.walk.chat_service.model.entity.ChatWithMembersEntity(
                        member.chatId,
                        array_agg(member.userId) within group (order by member.userId)
                    )
                    from ChatMemberEntity member
                    left join (
                        select chatId as chatIdAlias, max(sentAt) as lastMessageSentAt
                        from MessageEntity
                        group by chatId
                    ) message on member.chatId = message.chatIdAlias
                    where member.userId = :userId
                    group by member.chatId, message.lastMessageSentAt
                    order by case when message.lastMessageSentAt is null then 0 else 1 end, message.lastMessageSentAt desc
                    """
    )
    List<ChatWithMembersEntity> findAllChatWithMembersByUserId(UUID userId, Pageable pageable);

    @Query(
            """
                    select chatId
                    from ChatMemberEntity m
                    where userId in :userIds
                    group by chatId
                    having count(distinct userId) = :#{#userIds.size()}
                    """
    )
    Set<UUID> findAllChatIdWithUserIdsAsMembersForEach(Set<UUID> userIds);

    Optional<ChatMemberEntity> findAllByChatIdAndUserId(UUID chatId, UUID userId);

    List<ChatMemberEntity> findAllByChatId(UUID chatId);
}
