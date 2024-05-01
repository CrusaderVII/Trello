package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.exception.task.NoSuchTaskException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.*;

import java.util.List;

public interface TaskService {
    Task getTaskById(long id);
    Task saveTask(Task entity);
    User addUserToTask(long taskId, long userId);
    Task changeBoard(long taskId, long newBoardId);
    void removeUserFromTask(long taskId, long userId);
    void deleteTask(long id);
}
