package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dto.creation.DeskCreationDTO;

import java.util.UUID;

public interface DeskService {
    Desk getDeskById(UUID id);
    Desk updateDesk(Desk desk);
    Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO);
    void deleteDesk(UUID id);
}
