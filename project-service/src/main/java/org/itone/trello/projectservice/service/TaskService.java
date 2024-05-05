package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dto.creation.TaskCreationDTO;

import java.util.UUID;

public interface TaskService {
    Task getTaskById(UUID id);
    Task updateTask(Task task);
    Task addTaskToBoard(UUID boardId, TaskCreationDTO taskCreationDTO);
    User addUserToTask(UUID taskId, UUID userId);
    Task changeBoard(UUID taskId, UUID newBoardId);
    void removeUserFromTask(UUID taskId, UUID userId);
    void deleteTask(UUID id);
}
