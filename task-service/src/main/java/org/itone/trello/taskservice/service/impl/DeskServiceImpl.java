package org.itone.trello.taskservice.service.impl;

import org.itone.trello.taskservice.dao.DeskDAO;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;
import org.itone.trello.taskservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.service.DeskService;
import org.itone.trello.taskservice.util.exception.project.NoSuchProjectException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeskServiceImpl implements DeskService {

    private final DeskDAO deskDAO;

    public DeskServiceImpl(DeskDAO deskDAO) {
        this.deskDAO = deskDAO;
    }

    @Override
    public Desk getDeskById(UUID id) throws NoSuchDeskException{
        return deskDAO.findById(id);
    }

    @Override
    public Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO) throws NoSuchProjectException {
        return deskDAO.addDeskToProject(projectId, deskCreationDTO);
    }

    @Override
    public Desk updateDesk(Desk desk) {
        return deskDAO.save(desk);
    }

    @Override
    public void deleteDesk(UUID id) {
        deskDAO.deleteById(id);
    }
}
