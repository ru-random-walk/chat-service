package ru.random.walk.chat_service.service.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.entity.OutboxMessage;
import ru.random.walk.chat_service.repository.OutboxRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class OutboxExpireJob implements Job {
    private static final int OUTBOX_MESSAGE_TTL_IN_DAYS = 3;

    private final OutboxRepository outboxRepository;

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime deleteUntilDate = LocalDateTime.now().minusDays(OUTBOX_MESSAGE_TTL_IN_DAYS);
        log.info("Running outbox expire job. Deleting all messages created until {}", deleteUntilDate);

        List<OutboxMessage> messages = outboxRepository.getAllByCreatedAtBefore(deleteUntilDate);

        log.info("Deleting {} outbox messages", messages.size());
        outboxRepository.deleteAllInBatch(messages);
    }
}
