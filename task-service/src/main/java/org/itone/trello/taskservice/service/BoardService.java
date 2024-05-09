package org.itone.trello.taskservice.service;

import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dto.creation.BoardCreationDTO;

import java.util.UUID;

public interface BoardService {
    Board getBoardById(UUID id);
    Board updateBoard(Board board);
    Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO);
    void deleteBoard(UUID id);
}
