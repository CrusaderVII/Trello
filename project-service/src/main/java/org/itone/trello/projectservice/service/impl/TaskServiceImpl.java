package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.TaskRepository;
import org.itone.trello.projectservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getTaskById(long id) {
        return null;
    }

    @Override
    public List<User> getAllUsersOnTask(long projectId) {
        return null;
    }

    @Override
    public Board getBoardOfTasks(long taskId) {
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return null;
    }

    @Override
    public Task saveTask(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public Task updateTask(Task oldEntity) {
        return null;
    }

    @Override
    public void deleteTask(long id) {

    }
}
