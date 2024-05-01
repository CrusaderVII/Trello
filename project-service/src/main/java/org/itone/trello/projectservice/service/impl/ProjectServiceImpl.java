package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.ProjectRepository;
import org.itone.trello.projectservice.service.DeskService;
import org.itone.trello.projectservice.service.ProjectService;
import org.itone.trello.projectservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final DeskService deskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService, DeskService deskService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.deskService = deskService;
    }

    @Override
    public Project getProjectById(long id) throws NoSuchProjectException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchProjectException("id "+id));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project saveProject(Project entity) {
        return projectRepository.save(entity);
    }

    @Override
    public User addUserToProject(long projectId, long userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using corresponding services
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);

        //Add user to a set of users in project. Because project's addUser() method also
        //encapsulates adding current project to user, we don't need to addProject() method on user object
        project.addUser(user);

        //Save changes to project and user entities to DB. For user entity use updateUser() method,
        //because using saveUser() method will call UserValidationService inside
        saveProject(project);
        return  userService.updateUser(user);
    }

    @Override
    public Desk addDeskToProject(long projectId, Desk desk) throws NoSuchProjectException{
        //Get project by id using projectService
        Project project = getProjectById(projectId);

        //Add to set of desks of gotten project new desk. Desk can be created only in Project controller.
        //addDesk() method also encapsulates setting project of added desk to current project, so we don't need
        //to call setProject() method of desk object separately.
        project.addDesk(desk);

        //Save changes to project and desk entities to DB
        saveProject(project);
        return deskService.saveDesk(desk);
    }

    @Override
    public void removeUserFromProject(long projectId, long userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using corresponding services
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);

        //Call removeUser() method on project object. This method also encapsulates removing given project
        //from set of projects of given user, so we don't need to call method to remove project from user separately
        project.removeUser(user.getId());

        //Save changes to project and user entities to DB. For user entity use updateUser() method,
        //because using saveUser() method will call UserValidationService inside
        saveProject(project);
        userService.updateUser(user);
    }

    @Override
    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }
}
