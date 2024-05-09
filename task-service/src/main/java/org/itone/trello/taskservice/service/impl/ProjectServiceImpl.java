package org.itone.trello.taskservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.taskservice.dao.ProjectDAO;
import org.itone.trello.taskservice.dto.creation.ProjectCreationDTO;
import org.itone.trello.taskservice.util.exception.project.NoSuchProjectException;
import org.itone.trello.taskservice.util.exception.user.NoSuchUserException;
import org.itone.trello.taskservice.dao.model.Project;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public Project getProjectById(UUID id) throws NoSuchProjectException {
        return projectDAO.findById(id);
    }

    @Override
    public Project saveProject(ProjectCreationDTO projectCreationDTO) {
        //Create new Project object from gotten projectCreation dto
        Project project = Project.fromCreationDTO(projectCreationDTO);
        return projectDAO.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return projectDAO.save(project);
    }

    @Override
    public User addUserToProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        return projectDAO.addUserToProject(projectId, userId);
    }

    @Override
    public void removeUserFromProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        projectDAO.removeUserFromProject(projectId, userId);
    }

    @Override
    public void deleteProject(UUID id) {
        projectDAO.deleteById(id);
    }
}
