package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
