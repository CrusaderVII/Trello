package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;

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
