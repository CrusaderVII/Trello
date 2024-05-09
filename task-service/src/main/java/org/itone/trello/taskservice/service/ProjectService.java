package org.itone.trello.taskservice.service;

import org.itone.trello.taskservice.dao.model.Project;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.dto.creation.ProjectCreationDTO;

import java.util.UUID;

public interface ProjectService {
    Project getProjectById(UUID id);
    Project saveProject(ProjectCreationDTO projectCreationDTO);
    User addUserToProject(UUID projectId, UUID userId);
    Project updateProject(Project project);
    void removeUserFromProject(UUID projectId, UUID userId);
    void deleteProject(UUID id);
}
