package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.DeskDAO;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dto.creation.DeskCreationDTO;
import org.itone.trello.projectservice.service.ProjectService;
import org.itone.trello.projectservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.repository.DeskRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.itone.trello.projectservice.service.DeskService;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
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
