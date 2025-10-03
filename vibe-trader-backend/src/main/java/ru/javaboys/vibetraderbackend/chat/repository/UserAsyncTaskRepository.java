package ru.javaboys.vibetraderbackend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaboys.vibetraderbackend.chat.model.UserAsyncTask;

public interface UserAsyncTaskRepository extends JpaRepository<UserAsyncTask, Long> {
}
