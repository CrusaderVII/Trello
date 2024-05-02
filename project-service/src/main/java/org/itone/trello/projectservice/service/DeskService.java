package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;

public interface DeskService {
    Desk getDeskById(long id);
    Desk saveDesk(Desk entity);
    Board addBoardToDesk(long deskId, Board board);

    void deleteDesk(long id);
}
