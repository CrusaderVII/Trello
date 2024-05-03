package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;

import java.util.UUID;

public interface TaskService {
    Task getTaskById(UUID id);
    Task saveTask(Task entity);
    User addUserToTask(UUID taskId, UUID userId);
    Task changeBoard(UUID taskId, UUID newBoardId);
    void removeUserFromTask(UUID taskId, UUID userId);
    void deleteTask(UUID id);
}
