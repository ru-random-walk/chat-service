package ru.random.walk.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.model.entity.MessageEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    default MessageFilter toMessageFilter(String message, LocalDateTime from, LocalDateTime to) {
        return new MessageFilter(message, from, to);
    }

    @Mapping(source = "entity.sentAt", target = "createdAt")
    @Mapping(target = "payloadDto", expression = "java(null)")
    @Mapping(source = "chatId", target = "chatId")
    MessageDto toDto(MessageEntity entity);
}