package ru.random.walk.chat_service.model.dto.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.random.walk.chat_service.model.dto.response.message.payload.LocationDto;
import ru.random.walk.chat_service.model.dto.response.message.payload.RequestForWalkDto;
import ru.random.walk.chat_service.model.dto.response.message.payload.TextDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextDto.class, name = "text"),
        @JsonSubTypes.Type(value = RequestForWalkDto.class, name = "request_for_walk"),
        @JsonSubTypes.Type(value = LocationDto.class, name = "location"),
})
@AllArgsConstructor
@Data
public abstract class PayloadDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String type;
}
