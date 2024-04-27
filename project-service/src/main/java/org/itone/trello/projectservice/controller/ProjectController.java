package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.DeskServiceImpl;
import org.itone.trello.projectservice.service.impl.ProjectServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/project")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final DeskServiceImpl deskServiceImpl;

    public ProjectController(ProjectServiceImpl projectServiceImpl, UserServiceImpl userServiceImpl, DeskServiceImpl deskServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.deskServiceImpl = deskServiceImpl;
    }

    //TODO: try to change way of creating new desks, boards and tasks (noy inside parent controller)
    //TODO: ask about using @Transactional on controllers about remove and save entities
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

        Project project = projectServiceImpl.getProjectById(id);

        Set<UserDTO> userDTOs = project.getUsers().stream()
                .map(user -> new UserDTO(user.getId(),
                                         user.getName(),
                                         user.getEmail()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("get/{id}/desks")
    public ResponseEntity<Set<DeskDTO>> getDesksOnProject(@PathVariable long id) {
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
    }
    @PostMapping("add/user")
    public ResponseEntity<UserDTO> addUserToProject(@RequestParam long userId,
                                                    @RequestParam long projectId) {
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
        return new ResponseEntity<>(new UserDTO(user.getId(), user.getName(), user.getEmail()), HttpStatus.OK);

    }

    @PostMapping("add/desk")
    public ResponseEntity<DeskDTO> addUserToProject(@RequestParam long projectId,
                                                    @RequestBody Desk desk) {
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

        return new ResponseEntity<>(new DeskDTO(desk.getId(), desk.getName(), projectDTO), HttpStatus.OK);

    }
    @PostMapping("/save")
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody Project project) {
        Project savedProject = projectServiceImpl.saveProject(project);

        return new ResponseEntity<>(new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription()),
                HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProject(@PathVariable long id) {
        //TODO: use "delete" when deleting entity and use "remove" when deleting connections between entities
        projectServiceImpl.deleteProject(id);

        return new ResponseEntity<String>("Project with "+id+" deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{projectId}/user")
    public ResponseEntity<String> removeUserFromProject(@PathVariable long projectId,
                                                        @RequestParam long userId) {

        Project project = projectServiceImpl.getProjectById(projectId);
        User user = userServiceImpl.getUserById(userId);

        project.removeUser(user.getId());

        projectServiceImpl.saveProject(project);
        userServiceImpl.updateUser(user);

        return new ResponseEntity<>("User with id "+userId+" successfully removed from project "+projectId,
                HttpStatus.OK);
    }

}
