package org.itone.trello.projectservice.dao;

import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dto.creation.DeskCreationDTO;
import org.itone.trello.projectservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;

import java.util.UUID;

public interface DeskDAO {
    public Desk findById(UUID id);
    public Desk save(Desk desk);
    public Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO);
    public void deleteById(UUID id);
}
