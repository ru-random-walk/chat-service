package ru.random.walk.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    @Mapping(source = "chatMember.id.chatId", target = "id")
    ChatDto chatMembertoChatDto(ChatMemberEntity chatMemberEntity);
}
