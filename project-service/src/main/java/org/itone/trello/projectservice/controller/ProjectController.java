package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.util.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Project;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.service.ProjectService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    //Create logger with the name corresponding to ProjectController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
        logger.setLevel(Level.DEBUG);
    }

    //TODO: try to change way of creating new desks, boards and tasks (not inside parent controller)
    //TODO: delete get/all in userController (or implement pageable to it)
    //TODO: add docker
    //TODO: change ids to UUID
    //TODO: create toDto method in entities
    //TODO: @Transactional
    @GetMapping("/get/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable long id) {

        Project project = projectService.getProjectById(id);

        ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                               project.getName(),
                                               project.getDescription());

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);

    }
//    @GetMapping("/get/all")
//    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
//        //Get list of all projects
//        List<Project> projects = projectService.getAllProjects();
//
//        //Generate list of projectDTOs from gotten list of projects in stream
//        List<ProjectDTO> projectDTOs = projects.stream()
//                .map(project -> new ProjectDTO(project.getId(),
//                                               project.getName(),
//                                               project.getDescription()))
//                .toList();
//
//        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
//    }

    @GetMapping("get/{id}/users")
    public ResponseEntity<Set<UserDTO>> getUsersOnProject(@PathVariable long id) {

        Project project = projectService.getProjectById(id);

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
        Project project = projectService.getProjectById(id);
        //Create projectDTO from project
        ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                                   project.getName(),
                                                   project.getDescription());

        //Generate list of deskDTO in stream of gotten desks
        Set<DeskDTO> deskDTOs = project.getDesks().stream()
                .map(desk -> new DeskDTO(desk.getId(),
                        desk.getName(),
                        project.getName()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(deskDTOs, HttpStatus.OK);
    }

    //When somebody creates a new project, also generates project adres (project_id) in view,
    //that can be copied and then shared with other users. User can insert this adres into something like
    //search_project search field. If adres correct, then user will be added to this project
    @PostMapping("add/user")
    public ResponseEntity<UserDTO> addUserToProject(@RequestParam long userId,
                                                    @RequestParam long projectId) {

        User user = projectService.addUserToProject(projectId, userId);

            //return userDTO from added user
        UserDTO userDTO = new UserDTO(user.getId(),
                                          user.getName(),
                                          user.getEmail());

        logger.debug("User {} was added to project with id {}", user, projectId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("add/desk")
    public ResponseEntity<DeskDTO> addDeskToProject(@RequestParam long projectId,
                                                    @RequestBody Desk desk) {

        desk = projectService.addDeskToProject(projectId, desk);

        DeskDTO deskDTO = new DeskDTO(desk.getId(),
                                          desk.getName(),
                                          desk.getProject().getName());

        logger.debug("Desk {} was added to project with id {}", desk, projectId);
        return new ResponseEntity<>(deskDTO, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);

        ProjectDTO projectDTO = new ProjectDTO(project.getId(),
                                               project.getName(),
                                               project.getDescription());

        logger.debug("New project {} was created", savedProject);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);

        logger.debug("Project with id {} was deleted successfully", id);
        return new ResponseEntity<String>("Project with "+id+" deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{projectId}/user")
    public ResponseEntity<String> removeUserFromProject(@PathVariable long projectId,
                                                        @RequestParam long userId) {

        projectService.removeUserFromProject(projectId, userId);

        logger.debug("User with id {} was removed from project with id {}", userId, projectId);
        return new ResponseEntity<>("User with id "+userId+" successfully removed from project "+projectId,
                HttpStatus.OK);

    }

}
