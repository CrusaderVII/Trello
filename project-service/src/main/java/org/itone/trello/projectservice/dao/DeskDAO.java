package org.itone.trello.projectservice.dao;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.repository.DeskRepository;
import org.itone.trello.projectservice.dto.creation.DeskCreationDTO;
import org.itone.trello.projectservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class DeskDAO {
    private final DeskRepository deskRepository;
    private final ProjectDAO projectDAO;

    public DeskDAO(DeskRepository deskRepository, ProjectDAO projectDAO) {
        this.deskRepository = deskRepository;
        this.projectDAO = projectDAO;
    }

    public Desk findById(UUID id) throws NoSuchDeskException {
        return deskRepository.findById(id)
                .orElseThrow(() -> new NoSuchDeskException("id "+id));
    }

    public Desk save(Desk desk) {
        return deskRepository.save(desk);
    }

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

    public void deleteById(UUID id) {
        deskRepository.deleteById(id);
    }

}
