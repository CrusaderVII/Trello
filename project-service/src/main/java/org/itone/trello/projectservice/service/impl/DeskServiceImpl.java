package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
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
@Transactional
public class DeskServiceImpl implements DeskService {

    private final DeskRepository deskRepository;
    private final ProjectService projectService;

    public DeskServiceImpl(DeskRepository deskRepository, ProjectService projectService) {
        this.deskRepository = deskRepository;
        this.projectService = projectService;
    }

    @Override
    public Desk getDeskById(UUID id) throws NoSuchDeskException{
        return deskRepository.findById(id)
                .orElseThrow(() -> new NoSuchDeskException("id "+id));
    }

    @Override
    public Desk addDeskToProject(UUID projectId, DeskCreationDTO deskCreationDTO) throws NoSuchProjectException {
        //Get project by id using projectService
        Project project = projectService.getProjectById(projectId);
        //Create new Desk object from gotten DeskCreationDTO object from request
        Desk desk = Desk.fromCreationDTO(deskCreationDTO);

        //Add to set of desks of gotten project new desk.
        //addDesk() method also encapsulates setting project of added desk to current project, so we don't need
        //to call setProject() method of desk object separately.
        project.addDesk(desk);

        //Save changes of project to DB
        projectService.updateProject(project);
        //Use updateDesk() method to save changes of new desk entity to DB
        return updateDesk(desk);
    }

    @Override
    public Desk updateDesk(Desk desk) {
        return deskRepository.save(desk);
    }

    @Override
    public void deleteDesk(UUID id) {
        deskRepository.deleteById(id);
    }
}
