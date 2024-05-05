package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dto.creation.ProjectCreationDTO;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dao.repository.ProjectRepository;
import org.itone.trello.projectservice.service.DeskService;
import org.itone.trello.projectservice.service.ProjectService;
import org.itone.trello.projectservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Override
    public Project getProjectById(UUID id) throws NoSuchProjectException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchProjectException("id "+id));
    }

    @Override
    public Project saveProject(ProjectCreationDTO projectCreationDTO) {
        //Create new Project object from gotten projectCreation dto
        Project project = Project.fromCreationDTO(projectCreationDTO);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public User addUserToProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using corresponding services
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);

        //Add user to a set of users in project. Because project's addUser() method also
        //encapsulates adding current project to user, we don't need to addProject() method on user object
        project.addUser(user);

        //Save changes to project and user entities to DB. For user entity use updateUser() method,
        //because using saveUser() method will call UserValidationService inside
        updateProject(project);
        return  userService.updateUser(user);
    }

    @Override
    public void removeUserFromProject(UUID projectId, UUID userId) throws NoSuchProjectException, NoSuchUserException {
        //Get project and user by id using corresponding services
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);

        //Call removeUser() method on project object. This method also encapsulates removing given project
        //from set of projects of given user, so we don't need to call method to remove project from user separately
        project.removeUser(user.getId());

        //Save changes to project and user entities to DB. For user entity use updateUser() method,
        //because using saveUser() method will call UserValidationService inside
        updateProject(project);
        userService.updateUser(user);
    }

    @Override
    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }
}
