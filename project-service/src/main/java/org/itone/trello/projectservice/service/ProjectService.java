package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;

import java.util.List;

public interface ProjectService {
    Project getProjectById(long id);
    List<Project> getAllProjects();
    Project saveProject(Project entity);
    User addUserToProject(long projectId, long userId);
    Desk addDeskToProject(long projectId, Desk desk);
    void removeUserFromProject(long projectId, long userId);
    void deleteProject(long id);
}
