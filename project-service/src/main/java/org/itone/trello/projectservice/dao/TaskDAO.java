package org.itone.trello.projectservice.dao;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dto.creation.TaskCreationDTO;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.util.exception.task.NoSuchTaskException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;

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
