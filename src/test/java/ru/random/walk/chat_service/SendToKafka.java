package ru.random.walk.chat_service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.random.walk.dto.CreatePrivateChatEvent;
import ru.random.walk.topic.EventTopic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Slf4j
public class SendToKafka {
    public static void main(String[] args) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        var kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties));
        var list = IntStream.range(0, 1000)
                .mapToObj(ignored -> kafkaTemplate.send(EventTopic.CREATE_CHAT, getRandomMessage()))
                .toList();
        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();
    }

    private static @NotNull CreatePrivateChatEvent getRandomMessage() {
        return new CreatePrivateChatEvent(
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }
}
