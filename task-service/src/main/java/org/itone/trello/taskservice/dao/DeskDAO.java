package org.itone.trello.taskservice.dao;

import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;

import java.util.UUID;

public interface DeskDAO {
    public Desk findById(UUID id);
    public Desk save(Desk desk);
    public Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO);
    public void deleteById(UUID id);
}
