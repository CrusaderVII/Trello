package org.itone.trello.projectservice.dao;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dto.creation.BoardCreationDTO;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;

import java.util.UUID;

public interface BoardDAO {
    public Board findById(UUID id);
    public Board save(Board board);
    public Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO);
    public void deleteById(UUID id);
}
