package ru.random.walk.chat_service.model.domain.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LocationPayload.class, name = "location"),
        @JsonSubTypes.Type(value = RequestForWalkPayload.class, name = "request_for_walk"),
        @JsonSubTypes.Type(value = TextPayload.class, name = "text")
})
@AllArgsConstructor
@Getter
@Setter
public abstract class MessagePayload {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String type;
}