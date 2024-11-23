package ru.random.walk.chat_service.controller.format;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Inherited
@DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
@Schema(example = "18:00 22-09-2024")
public @interface GlobalDateTimeFormat {
    String DEFAULT_PATTERN = "HH:mm dd-MM-yyyy";
}
