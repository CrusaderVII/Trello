package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.ProjectRepository;
import org.itone.trello.projectservice.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project getProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchProjectException("id "+id));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<User> getAllUsersOnProject(long projectId) {
        return projectRepository.findAllUsersOnProject(projectId);
    }

    @Override
    public List<Desk> getAllDesksOnProject(long projectId) {
        return projectRepository.findAllDesksOnProject(projectId);
    }

    @Override
    public Project saveProject(Project entity) {
        return projectRepository.save(entity);
    }

    @Override
    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }
}
