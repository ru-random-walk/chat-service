package ru.random.walk.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.random.walk.chat_service.model.entity.MessageEntity;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, UUID> {
    @Query(
            value = """
                    select * from chat.message
                    where (
                        chat_id = :chatId and
                        payload->>'text' like :message and
                        (cast(:from as timestamp) is null or sent_at >= :from) and
                        (cast(:to as timestamp) is null or sent_at <= :to)
                    )
                    """,
            nativeQuery = true
    )
    Page<MessageEntity> findByChatId(
            UUID chatId,
            @Param("message") @Nullable String message,
            @Param("from") @Nullable LocalDateTime from,
            @Param("to") @Nullable LocalDateTime to,
            Pageable pageable
    );

    MessageEntity save(MessageEntity message);

    Optional<MessageEntity> findById(UUID id);
}