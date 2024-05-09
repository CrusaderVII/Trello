package org.itone.trello.taskservice.dao.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.taskservice.dao.DeskDAO;
import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.dao.model.Project;
import org.itone.trello.taskservice.dao.repository.DeskRepository;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;
import org.itone.trello.taskservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.taskservice.util.exception.project.NoSuchProjectException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class DeskDAOImpl implements DeskDAO {
    private final DeskRepository deskRepository;
    private final ProjectDAOImpl projectDAO;

    public DeskDAOImpl(DeskRepository deskRepository, ProjectDAOImpl projectDAO) {
        this.deskRepository = deskRepository;
        this.projectDAO = projectDAO;
    }

    @Override
    public Desk findById(UUID id) throws NoSuchDeskException {
        return deskRepository.findById(id)
                .orElseThrow(() -> new NoSuchDeskException("id "+id));
    }

    @Override
    public Desk save(Desk desk) {
        return deskRepository.save(desk);
    }

    @Override
    public Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO) throws NoSuchProjectException {
        //Get project by id using projectService
        Project project = projectDAO.findById(projectId);
        //Create new Desk object from gotten DeskCreationDTO object from request
        Desk desk = Desk.fromCreationDTO(deskCreationDTO);

        //Add to set of desks of gotten project new desk.
        //addDesk() method also encapsulates setting project of added desk to current project, so we don't need
        //to call setProject() method of desk object separately.
        project.addDesk(desk);

        //Save changes of project to DB
        projectDAO.save(project);
        //Save changes of new desk entity to DB
        return save(desk);
    }

    @Override
    public void deleteById(UUID id) {
        deskRepository.deleteById(id);
    }

}
