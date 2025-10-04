package ru.javaboys.vibetraderbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.chat.model.TaskStatus;
import ru.javaboys.vibetraderbackend.chat.model.UserAsyncTask;
import ru.javaboys.vibetraderbackend.chat.repository.UserAsyncTaskRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessor {

    private final UserAsyncTaskRepository taskRepository;

    @Transactional
    public void processTask(Long taskId) {
        UserAsyncTask task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            log.warn("Задача {} не найдена для обработки", taskId);
            return;
        }
        if (task.getStatus() != TaskStatus.RUNNING) {
            log.info("Задача {} не в статусе RUNNING (текущий статус={}), пропуск", taskId, task.getStatus());
            return;
        }

        try {
            log.info("Начинаю обработку задачи {}", taskId);

//            TaskResult result = optimizerAgent.optimize(task);
//            task.setResult(result);
//            task.setStatus(TaskStatus.DONE);

            taskRepository.save(task);
            log.info("Задача {} успешно обработана", taskId);
        } catch (Exception e) {
            log.error("Ошибка при обработке задачи {}: {}", taskId, e.getMessage(), e);
            task.setStatus(TaskStatus.ERROR);
            task.setErrorMessage(e.getMessage());
            taskRepository.save(task);
        }
    }
}
