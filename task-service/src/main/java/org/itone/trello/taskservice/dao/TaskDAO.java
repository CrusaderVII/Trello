package org.itone.trello.taskservice.dao;

import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.dto.creation.TaskCreationDTO;

import java.util.UUID;

public interface TaskDAO {
    public Task findById(UUID id);
    public Task save(Task task);
    public Task addTaskToBoard(UUID boardId, TaskCreationDTO taskCreationDTO);
    public User addUserToTask(UUID taskId, UUID userId);
    public Task changeBoard(UUID taskId, UUID newBoardId);
    public void removeUserFromTask(UUID taskId, UUID userId);
    public void deleteById(UUID id);
}
