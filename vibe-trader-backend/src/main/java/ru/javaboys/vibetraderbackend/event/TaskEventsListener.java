package ru.javaboys.vibetraderbackend.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.javaboys.vibetraderbackend.service.TaskProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventsListener {

    private final TaskProcessor taskProcessor;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onTaskCreated(TaskCreatedEvent event) {
        try {
            log.info("Получено событие TaskCreatedEvent для задачи id={}", event.taskId());
            taskProcessor.processTask(event.taskId());
        } catch (Exception e) {
            log.error("Асинхронная обработка задачи id={} завершилась ошибкой: {}", event.taskId(), e.getMessage(), e);
        }
    }
}
