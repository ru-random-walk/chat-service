package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.chat_service.model.entity.ChatMember;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMemberRepository extends PagingAndSortingRepository<ChatMember, ChatMember.ChatMemberId> {
    @Query(
            value = """
                    SELECT chatMember FROM ChatMember chatMember
                    WHERE chatMember.id.userId = :userId
                    """
    )
    List<ChatMember> findPageByUserId(UUID userId, Pageable pageable);

    @Query(
            value = """
                    SELECT COUNT(chatMember) FROM ChatMember chatMember
                    WHERE chatMember.id.userId = :userId
                    """
    )
    Long findTotalCountByUserId(UUID userId);
}
