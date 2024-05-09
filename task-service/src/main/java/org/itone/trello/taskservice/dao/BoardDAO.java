package org.itone.trello.taskservice.dao;

import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dto.creation.BoardCreationDTO;

import java.util.UUID;

public interface BoardDAO {
    public Board findById(UUID id);
    public Board save(Board board);
    public Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO);
    public void deleteById(UUID id);
}
