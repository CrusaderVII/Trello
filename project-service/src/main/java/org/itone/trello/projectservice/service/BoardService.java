package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;

public interface BoardService {
    Board getBoardById(long id);
    Board saveBoard(Board entity);
    Task addTask(long boardId, Task task);
    void deleteBoard(long id);
}
