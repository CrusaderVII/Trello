package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.DeskServiceImpl;
import org.itone.trello.projectservice.service.impl.ProjectServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/project")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final DeskServiceImpl deskServiceImpl;

    //Create logger with the name corresponding to ProjectController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectServiceImpl projectServiceImpl, UserServiceImpl userServiceImpl, DeskServiceImpl deskServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.deskServiceImpl = deskServiceImpl;
        logger.setLevel(Level.INFO);
    }

    //TODO: try to change way of creating new desks, boards and tasks (not inside parent controller)
    //TODO: ask about using @Transactional on controllers to remove and save entities???
    //TODO: delete get/all controllers (or implement pageable to them)
    @GetMapping("/get/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable long id) {
        Project project = projectServiceImpl.getProjectById(id);

        return new ResponseEntity<>(new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription()),
                HttpStatus.OK);
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        //Get list of all projects
        List<Project> projects = projectServiceImpl.getAllProjects();

        //Generate list of projectDTOs from gotten list of projects in stream
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(project -> new ProjectDTO(project.getId(),
                                               project.getName(),
                                               project.getDescription()))
                .toList();

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    @GetMapping("get/{id}/users")
    public ResponseEntity<Set<UserDTO>> getUsersOnProject(@PathVariable long id) {
        try {
            Project project = projectServiceImpl.getProjectById(id);

            Set<UserDTO> userDTOs = project.getUsers().stream()
                    .map(user -> new UserDTO(user.getId(),
                                             user.getName(),
                                             user.getEmail()))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (NoSuchProjectException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get/{id}/desks")
    public ResponseEntity<Set<DeskDTO>> getDesksOnProject(@PathVariable long id) {
        try {
            //Get project from projectServiceImpl by id
            Project project = projectServiceImpl.getProjectById(id);
            //Create projectDTO from project
            ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                                   project.getName(),
                                                   project.getDescription());

            //Generate list of deskDTO in stream of gotten desks
            Set<DeskDTO> deskDTOs = project.getDesks().stream()
                    .map(desk -> new DeskDTO(desk.getId(),
                                             desk.getName(),
                                             projectDTO))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(deskDTOs, HttpStatus.OK);
        } catch (NoSuchProjectException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("add/user")
    public ResponseEntity<UserDTO> addUserToProject(@RequestParam long userId,
                                                    @RequestParam long projectId) {
        try {
            //Get project and user by id using corresponding serviceImpl
            Project project = projectServiceImpl.getProjectById(projectId);
            User user = userServiceImpl.getUserById(userId);

            //Add user to a set of users in project. Because project's addUser() method also
            //encapsulates adding current project to user, we don't need to addProject() method
            //on user object
            project.addUser(user);

            //Save changes to project and user entities to DB. For user entity use updateUser() method,
            //because using saveUser() method will call UserValidationService inside
            projectServiceImpl.saveProject(project);
            userServiceImpl.updateUser(user);

            //return userDTO from added user
            logger.info("User {} was added to project {}", user, project);
            return new ResponseEntity<>(new UserDTO(user.getId(), user.getName(), user.getEmail()), HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoSuchProjectException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("add/desk")
    public ResponseEntity<DeskDTO> addUserToProject(@RequestParam long projectId,
                                                    @RequestBody Desk desk) {
        try {
            //Get project by id using projectServiceImpl
            Project project = projectServiceImpl.getProjectById(projectId);

            //Add to set of desks of gotten project new desk. Desk can be created only in this (Project) controller.
            //addDesk() method also encapsulates setting project of added desk to current project, so we don't need
            //to call setProject() method of desk object separately.
            project.addDesk(desk);

            //Save changes to project and desk entities to DB
            deskServiceImpl.saveDesk(desk);
            projectServiceImpl.saveProject(project);

            //Generate projectDTO from added desk
            ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                                   project.getName(),
                                                   project.getDescription());

            logger.info("Desk {} was added to project {}", desk, project);
            return new ResponseEntity<>(new DeskDTO(desk.getId(), desk.getName(), projectDTO), HttpStatus.OK);
        } catch (NoSuchProjectException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/save")
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody Project project) {
        Project savedProject = projectServiceImpl.saveProject(project);

        ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                               project.getName(),
                                               project.getDescription());

        logger.info("New project {} was created", savedProject);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable long id) {
        projectServiceImpl.deleteProject(id);

        logger.info("Project with id {} was deleted successfully", id);
        return new ResponseEntity<String>("Project with "+id+" deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{projectId}/user")
    public ResponseEntity<String> removeUserFromProject(@PathVariable long projectId,
                                                        @RequestParam long userId) {
        try {
            Project project = projectServiceImpl.getProjectById(projectId);
            User user = userServiceImpl.getUserById(userId);

            project.removeUser(user.getId());

            projectServiceImpl.saveProject(project);
            userServiceImpl.updateUser(user);

            logger.info("User {} was removed from project {}", user, project);
            return new ResponseEntity<>("User with id "+userId+" successfully removed from project "+projectId,
                    HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoSuchProjectException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
