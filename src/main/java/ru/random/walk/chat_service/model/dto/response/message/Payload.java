package ru.random.walk.chat_service.model.dto.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.random.walk.chat_service.model.dto.response.message.payload.Location;
import ru.random.walk.chat_service.model.dto.response.message.payload.RequestForWalk;
import ru.random.walk.chat_service.model.dto.response.message.payload.Text;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Text.class, name = "text"),
        @JsonSubTypes.Type(value = RequestForWalk.class, name = "request_for_walk"),
        @JsonSubTypes.Type(value = Location.class, name = "location"),
})
@AllArgsConstructor
@Data
public abstract class Payload {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String type;
}
