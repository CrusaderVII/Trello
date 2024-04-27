package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;

import java.util.List;

public interface ProjectService {
    Project getProjectById(long id);
    List<Project> getAllProjects();
    Project saveProject(Project entity);
    void deleteProject(long id);
}
