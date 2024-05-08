package org.itone.trello.projectservice.dao.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.ProjectDAO;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dao.repository.ProjectRepository;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class ProjectDAOImpl implements ProjectDAO {
    private final ProjectRepository projectRepository;
    private final UserDAOImpl userDAO;

    public ProjectDAOImpl(ProjectRepository projectRepository, UserDAOImpl userDAO) {
        this.projectRepository = projectRepository;
        this.userDAO = userDAO;
    }

    @Override
    public Project findById(UUID id) throws NoSuchProjectException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchProjectException("id "+id));
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public User addUserToProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using repository and DAO (for user)
        Project project = findById(projectId);
        User user = userDAO.findById(userId);

        //Add user to a set of users in project. Because project's addUser() method also
        //encapsulates adding current project to user, we don't need to addProject() method on user object
        project.addUser(user);

        //Save changes to project and user entities to DB.
        save(project);
        return userDAO.save(user);
    }

    @Override
    public void removeUserFromProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using repository and DAO (for user)
        Project project = findById(projectId);
        User user = userDAO.findById(userId);

        //Call removeUser() method on project object. This method also encapsulates removing given project
        //from set of projects of given user, so we don't need to call method to remove project from user separately
        project.removeUser(user.getId());

        //Save changes to project and user entities to DB. For user entity use updateUser() method,
        //because using saveUser() method will call UserValidationService inside
        save(project);
        userDAO.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        projectRepository.deleteById(id);
    }
}
