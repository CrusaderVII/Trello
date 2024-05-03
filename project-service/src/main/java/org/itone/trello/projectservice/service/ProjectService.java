package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    Project getProjectById(UUID id);
    List<Project> getAllProjects();
    Project saveProject(Project entity);
    User addUserToProject(UUID projectId, UUID userId);
    Desk addDeskToProject(UUID projectId, Desk desk);
    void removeUserFromProject(UUID projectId, UUID userId);
    void deleteProject(UUID id);
}
