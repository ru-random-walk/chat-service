package ru.random.walk.chat_service.service.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.model.domain.OutboxAdditionalInfoKey;
import ru.random.walk.chat_service.model.domain.OutboxHttpTopic;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.dto.matcher.AppointmentDetailsDto;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;
import ru.random.walk.chat_service.model.entity.AppointmentEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.OutboxMessage;
import ru.random.walk.chat_service.repository.AppointmentRepository;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.repository.OutboxRepository;
import ru.random.walk.chat_service.service.client.MatcherClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class OutboxSendingJob implements Job {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MatcherClient matcherClient;
    private final ObjectMapper objectMapper;
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        List<OutboxMessage> messages = outboxRepository.findAllBySentFalse();
        messages.forEach(this::tryToSendMessage);
        outboxRepository.saveAll(messages);
    }

    private void tryToSendMessage(OutboxMessage message) {
        try {
            var httpTopic = OutboxHttpTopic.getValueOf(message.getTopic());
            if (httpTopic.isPresent()) {
                tryToSendHttpRequest(httpTopic.get(), message);
            } else {
                tryToSendEvent(message);
            }
            message.setSent(true);
        } catch (Exception e) {
            log.error("Error sending outbox message with id %s".formatted(message.getId()), e);
        }
    }

    private void tryToSendHttpRequest(OutboxHttpTopic topic, OutboxMessage message) throws JsonProcessingException {
        if (topic == OutboxHttpTopic.SEND_CREATING_APPOINTMENT_TO_MATCHER) {
            var dto = objectMapper.readValue(message.getPayload(), RequestForAppointmentDto.class);
            var messageEntity = Optional.of(message.getAdditionalInfo().get(OutboxAdditionalInfoKey.MESSAGE_ID.name()))
                    .map(UUID::fromString)
                    .flatMap(messageRepository::findById)
                    .orElseThrow();
            var appointmentDetailsDto = matcherClient.requestForAppointment(dto);
            attachAppointmentIdToMessage(messageEntity, appointmentDetailsDto);
            appointmentRepository.save(AppointmentEntity.builder()
                    .appointmentId(appointmentDetailsDto.id())
                    .messageId(messageEntity.getId())
                    .build());
        }
    }

    private void attachAppointmentIdToMessage(MessageEntity messageEntity, AppointmentDetailsDto appointmentDetailsDto) {
        var requestForWalkPayload = (RequestForWalkPayload) messageEntity.getPayload();
        requestForWalkPayload.setAppointmentId(appointmentDetailsDto.id());
        messageEntity.setPayload(requestForWalkPayload);
        messageRepository.save(messageEntity);
    }

    private void tryToSendEvent(OutboxMessage message) {
        var topic = message.getTopic();
        var payload = message.getPayload();
        log.info("Sending message to {}, payload: {}", topic, payload);
        kafkaTemplate.send(topic, payload);
    }
}
