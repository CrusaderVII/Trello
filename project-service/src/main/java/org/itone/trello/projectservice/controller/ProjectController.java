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
import java.util.UUID;
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
    //TODO: @Transactional

    @GetMapping("/get/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable UUID id) {
        Project project = projectService.getProjectById(id);

        return new ResponseEntity<>(project.toDTO(), HttpStatus.OK);

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
    public ResponseEntity<Set<UserDTO>> getUsersOnProject(@PathVariable UUID id) {

        Project project = projectService.getProjectById(id);

        Set<UserDTO> userDTOs = project.getUsers().stream()
                .map(User::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("get/{id}/desks")
    public ResponseEntity<Set<DeskDTO>> getDesksOnProject(@PathVariable UUID id) {
        //Get project from projectServiceImpl by id
        Project project = projectService.getProjectById(id);

        //Generate list of deskDTO in stream of gotten desks
        Set<DeskDTO> deskDTOs = project.getDesks().stream()
                .map(Desk::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(deskDTOs, HttpStatus.OK);
    }

    //When somebody creates a new project, also generates project adres (project_id) in view,
    //that can be copied and then shared with other users. User can insert this adres into something like
    //search_project search field. If adres correct, then user will be added to this project
    @PostMapping("add/user")
    public ResponseEntity<UserDTO> addUserToProject(@RequestParam UUID userId,
                                                    @RequestParam UUID projectId) {

        User user = projectService.addUserToProject(projectId, userId);

        logger.debug("User {} was added to project with id {}", user, projectId);
        return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
    }

    @PostMapping("add/desk")
    public ResponseEntity<DeskDTO> addDeskToProject(@RequestParam UUID projectId,
                                                    @RequestBody Desk desk) {

        desk = projectService.addDeskToProject(projectId, desk);

        logger.debug("Desk {} was added to project with id {}", desk, projectId);
        return new ResponseEntity<>(desk.toDTO(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ProjectDTO> saveProject(@RequestBody Project project) {
        Project savedProject = projectService.saveProject(project);

        logger.debug("New project {} was created", savedProject);
        return new ResponseEntity<>(project.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);

        logger.debug("Project with id {} was deleted successfully", id);
        return new ResponseEntity<String>("Project with "+id+" deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/remove/{projectId}/user")
    public ResponseEntity<String> removeUserFromProject(@PathVariable UUID projectId,
                                                        @RequestParam UUID userId) {

        projectService.removeUserFromProject(projectId, userId);

        logger.debug("User with id {} was removed from project with id {}", userId, projectId);
        return new ResponseEntity<>("User with id "+userId+" successfully removed from project "+projectId,
                HttpStatus.OK);

    }

}
