package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dto.creation.BoardCreationDTO;

import java.util.UUID;

public interface BoardService {
    Board getBoardById(UUID id);
    Board updateBoard(Board board);
    Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO);
    void deleteBoard(UUID id);
}
