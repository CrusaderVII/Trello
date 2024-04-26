package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.User;

import java.util.List;

public interface DeskService {
    Desk getDeskById(long id);
    List<Desk> getAllDesks();
    Desk saveDesk(Desk entity);
    void deleteDesk(long id);
}
