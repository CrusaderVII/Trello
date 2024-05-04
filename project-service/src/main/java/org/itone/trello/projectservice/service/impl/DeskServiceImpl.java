package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.model.Project;
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
    public Desk addDeskToProject(UUID projectId, Desk desk) throws NoSuchProjectException {
        //Get project by id using projectService
        Project project = projectService.getProjectById(projectId);

        //Add to set of desks of gotten project new desk.
        //addDesk() method also encapsulates setting project of added desk to current project, so we don't need
        //to call setProject() method of desk object separately.
        project.addDesk(desk);

        //Save changes to project and desk entities to DB
        projectService.saveProject(project);
        return saveDesk(desk);
    }

    @Override
    public Desk saveDesk(Desk entity) {
        return deskRepository.save(entity);
    }

    @Override
    public void deleteDesk(UUID id) {
        deskRepository.deleteById(id);
    }
}
