package org.itone.trello.taskservice.dao;

import org.itone.trello.taskservice.dao.model.Project;
import org.itone.trello.taskservice.dao.model.User;

import java.util.UUID;

public interface ProjectDAO {

    public Project findById(UUID id);

    public Project save(Project project);

    public User addUserToProject(UUID projectId, UUID userId);

    public void removeUserFromProject(UUID projectId, UUID userId);

    public void deleteById(UUID id);
}
