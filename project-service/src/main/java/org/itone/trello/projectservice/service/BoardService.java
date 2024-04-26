package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Task;

import java.util.List;

public interface BoardService {
    Board getBoardById(long id);
    Desk getDeskOfBoard(long boardId);
    List<Task> getAllTasksOnBoard(long boardId);
    Board saveBoard(Board entity);
    void deleteBoard(long id);
}
