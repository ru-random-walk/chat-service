package ru.random.walk.chat_service.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-04T01:32:36+0300",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.1.jar, environment: Java 21.0.3 (Amazon.com Inc.)"
)
@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatDto chatMembertoChatDto(ChatMemberEntity chatMember) {
        if ( chatMember == null ) {
            return null;
        }

        ChatDto.ChatDtoBuilder chatDto = ChatDto.builder();

        chatDto.id( chatMemberIdChatId( chatMember ) );

        return chatDto.build();
    }

    private UUID chatMemberIdChatId(ChatMemberEntity chatMember) {
        ChatMemberEntity.ChatMemberId id = chatMember.getId();
        if ( id == null ) {
            return null;
        }
        return id.getChatId();
    }
}
