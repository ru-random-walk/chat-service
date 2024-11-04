package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends PagingAndSortingRepository<ChatMemberEntity, ChatMemberEntity.ChatMemberId> {
    @Query(
            value = """
                    SELECT chatMemberEntity FROM ChatMemberEntity chatMemberEntity
                    WHERE chatMemberEntity.id.userId = :userId
                    """
    )
    List<ChatMemberEntity> findPageByUserId(UUID userId, Pageable pageable);

    @Query(
            value = """
                    SELECT COUNT(chatMemberEntity) FROM ChatMemberEntity chatMemberEntity
                    WHERE chatMemberEntity.id.userId = :userId
                    """
    )
    Long findTotalCountByUserId(UUID userId);
}
