package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;

import java.util.UUID;

public interface BoardService {
    Board getBoardById(UUID id);
    Board saveBoard(Board entity);
    Board addBoardToDesk(UUID deskId, Board board);
    void deleteBoard(UUID id);
}
