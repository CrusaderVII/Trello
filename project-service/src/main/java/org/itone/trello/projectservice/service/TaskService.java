package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;

public interface TaskService {
    Task getTaskById(long id);
    Task saveTask(Task entity);
    User addUserToTask(long taskId, long userId);
    Task changeBoard(long taskId, long newBoardId);
    void removeUserFromTask(long taskId, long userId);
    void deleteTask(long id);
}
