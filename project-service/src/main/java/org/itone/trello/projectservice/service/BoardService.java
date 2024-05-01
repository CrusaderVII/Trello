package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Task;

import java.util.List;

public interface BoardService {
    Board getBoardById(long id);
    Board saveBoard(Board entity);
    Task addTask(long boardId, Task task);
    void deleteBoard(long id);
}
