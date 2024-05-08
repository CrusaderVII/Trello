package org.itone.trello.projectservice.dao;

import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;

import java.util.UUID;

public interface ProjectDAO {

    public Project findById(UUID id);

    public Project save(Project project);

    public User addUserToProject(UUID projectId, UUID userId);

    public void removeUserFromProject(UUID projectId, UUID userId);

    public void deleteById(UUID id);
}
