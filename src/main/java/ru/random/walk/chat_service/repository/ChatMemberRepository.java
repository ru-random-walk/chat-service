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
                    select new ru.random.walk.chat_service.model.entity.ChatWithMembersEntity(m.chatId, array_agg(m.userId) within group (order by m.userId))
                    from ChatMemberEntity m
                    where m.chatId in (
                        select member.chatId
                        from ChatMemberEntity member
                        where member.userId = :userId
                    )
                    group by m.chatId
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
}
