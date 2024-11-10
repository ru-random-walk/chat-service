package ru.random.walk.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;
import ru.random.walk.chat_service.model.dto.response.message.payload.LocationDto;
import ru.random.walk.chat_service.model.dto.response.message.payload.RequestForWalkDto;
import ru.random.walk.chat_service.model.dto.response.message.payload.TextDto;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.payload.LocationPayload;
import ru.random.walk.chat_service.model.entity.payload.MessagePayload;
import ru.random.walk.chat_service.model.entity.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.entity.payload.TextPayload;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    default MessageFilter toMessageFilter(String message, LocalDateTime from, LocalDateTime to) {
        return new MessageFilter(message, from, to);
    }

    @Mapping(source = "entity.sentAt", target = "createdAt")
    @Mapping(source = "payload", target = "payloadDto")
    @Mapping(source = "chatId", target = "chatId")
    MessageDto toDto(MessageEntity entity);

    default PayloadDto map(MessagePayload payload) {
        return switch (payload) {
            case TextPayload textPayload -> new TextDto(textPayload.getText());
            case RequestForWalkPayload requestForWalkPayload -> new RequestForWalkDto(
                    new LocationDto(
                            requestForWalkPayload.getLocation().getLongitude(),
                            requestForWalkPayload.getLocation().getLatitude()
                    ),
                    requestForWalkPayload.getStartsAt(),
                    requestForWalkPayload.getAnswer()
            );
            case LocationPayload locationPayload -> new LocationDto(
                    locationPayload.getLongitude(),
                    locationPayload.getLatitude()
            );
            default -> throw new IllegalStateException("Unexpected value: " + payload);
        };
    }
}
