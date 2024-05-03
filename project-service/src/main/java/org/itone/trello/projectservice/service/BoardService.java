package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;

import java.util.UUID;

public interface BoardService {
    Board getBoardById(UUID id);
    Board saveBoard(Board entity);
    Task addTask(UUID boardId, Task task);
    void deleteBoard(UUID id);
}
