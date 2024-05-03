package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;

import java.util.UUID;

public interface DeskService {
    Desk getDeskById(UUID id);
    Desk saveDesk(Desk entity);
    Board addBoardToDesk(UUID deskId, Board board);

    void deleteDesk(UUID id);
}
