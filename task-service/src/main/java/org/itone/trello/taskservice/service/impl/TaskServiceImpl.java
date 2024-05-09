package org.itone.trello.taskservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.taskservice.dao.TaskDAO;
import org.itone.trello.taskservice.dto.creation.TaskCreationDTO;
import org.itone.trello.taskservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.taskservice.util.exception.task.NoSuchTaskException;
import org.itone.trello.taskservice.util.exception.user.NoSuchUserException;
import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;

    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public Task getTaskById(UUID id) throws NoSuchTaskException{
        return taskDAO.findById(id);
    }

    @Override
    public Task updateTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    public Task addTaskToBoard(UUID boardId, TaskCreationDTO taskCreationDTO) throws NoSuchBoardException{
        return taskDAO.addTaskToBoard(boardId, taskCreationDTO);
    }

    @Override
    public User addUserToTask(UUID taskId, UUID userId) throws NoSuchTaskException, NoSuchUserException {
        return taskDAO.addUserToTask(taskId, userId);
    }

    @Override
    public Task changeBoard(UUID taskId, UUID newBoardId) throws NoSuchTaskException, NoSuchBoardException {
        return taskDAO.changeBoard(taskId, newBoardId);
    }

    @Override
    public void removeUserFromTask(UUID taskId, UUID userId) throws NoSuchTaskException, NoSuchUserException {
        taskDAO.removeUserFromTask(taskId, userId);
    }

    @Override
    public void deleteTask(UUID id) {
        taskDAO.deleteById(id);
    }
}
