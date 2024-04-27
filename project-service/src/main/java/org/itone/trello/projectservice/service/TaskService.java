package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.*;

import java.util.List;

public interface TaskService {
    Task getTaskById(long id);
    Task saveTask(Task entity);
    void deleteTask(long id);
}
