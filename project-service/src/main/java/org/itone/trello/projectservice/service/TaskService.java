package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.*;

import java.util.List;

public interface TaskService {
    Task getTaskById(long id);
    List<User> getAllUsersOnTask(long projectId);
    Board getBoardOfTasks(long taskId);
    List<Task> getAllTasks();
    Task saveTask(Task entity);
    Task updateTask(Task oldEntity);
    void deleteTask(long id);
}
