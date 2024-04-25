package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.ProjectServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trello/api/v1/project")
public class ProjectController {

    private final ProjectServiceImpl projectServiceImpl;
    private final UserServiceImpl userServiceImpl;
    public ProjectController(ProjectServiceImpl projectServiceImpl, UserServiceImpl userServiceImpl) {
        this.projectServiceImpl = projectServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

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
        List<Project> projects = projectServiceImpl.getAllProjects();

        List<ProjectDTO> projectDTOs = projects.stream()
                .map(project -> new ProjectDTO(project.getId(), project.getName(), project.getDescription()))
                .toList();

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    @GetMapping("get/{id}/users")
    public ResponseEntity<List<UserDTO>> getUsersOnProject(@PathVariable long id) {
        List<User> users = projectServiceImpl.getAllUsersOnProject(id);
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .toList();

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PostMapping("add/user")
    public ResponseEntity<UserDTO> addUserToProject(@RequestParam long userId,
                                                          @RequestParam long projectId) {
        Project project = projectServiceImpl.getProjectById(projectId);
        User user = userServiceImpl.getUserById(userId);

        if (!project.getUsers().contains(user)) {
            project.addUser(user);
            user.addProject(project);

            projectServiceImpl.saveProject(project);
            userServiceImpl.saveUser(user);
        }
        return new ResponseEntity<>(new UserDTO(user.getId(), user.getName(), user.getEmail()), HttpStatus.OK);

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable long id) {
        projectServiceImpl.deleteProject(id);

        return new ResponseEntity<String>("Project with "+id+" deleted successfully", HttpStatus.OK);
    }

}
