package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.User;

import java.util.List;

public interface DeskService {
    Desk getDeskById(long id);
    Desk saveDesk(Desk entity);
    void deleteDesk(long id);
}
