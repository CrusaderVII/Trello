package org.itone.trello.taskservice.service;

import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;

import java.util.UUID;

public interface DeskService {
    Desk getDeskById(UUID id);
    Desk updateDesk(Desk desk);
    Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO);
    void deleteDesk(UUID id);
}
