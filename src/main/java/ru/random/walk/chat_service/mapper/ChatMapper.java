package ru.random.walk.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatWithMembersEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    List<ChatDto> toDto(List<ChatWithMembersEntity> entities);

    @Mapping(target = "id", source = "chatId")
    ChatDto toDto(ChatWithMembersEntity entity);
}
