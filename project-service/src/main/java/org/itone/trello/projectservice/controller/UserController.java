package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.exception.user.WrongPasswordException;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.ProjectServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final ProjectServiceImpl projectServiceImpl;

    public UserController(UserServiceImpl userServiceImpl, ProjectServiceImpl projectServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.projectServiceImpl = projectServiceImpl;
    }

    //TODO: InvalidDataException handler. What is right way to implement it???
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        User user = userServiceImpl.getUserById(id);

        return new ResponseEntity<>(new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()),
                HttpStatus.OK);
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userServiceImpl.getAllUsers();

        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .toList();

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/projects")
    public ResponseEntity<Set<ProjectDTO>> getProjectsOfUser(@PathVariable long id) {
        User user = userServiceImpl.getUserById(id);

        Set<ProjectDTO> projectDTOs = user.getProjects().stream()
                .map( project -> new ProjectDTO(project.getId(), project.getName(), project.getDescription()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<UserDTO> authUser(@RequestParam String email,
                                            @RequestParam String password) {

        try {
            //Call userServiceImpl, that encapsulates finding user by email and then
            //using BCryptPasswordEncoder check if passwords match. In case wrong email or password
            //exception will be thrown
            User user = userServiceImpl.authUser(email, password);
            UserDTO userDTO = new UserDTO(user.getId(),
                    user.getName(),
                    user.getEmail());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (WrongPasswordException exc) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //When somebody creates a new project, also generates project adres (project_id) in view,
    //that can be copied and then shared with other users. User can insert this adres into something like
    //search_project search field. If adres correct, then user will be added to this project
    @PostMapping("/add/{userId}/project")
    public ResponseEntity<ProjectDTO> addExistingProjectByAdres(@PathVariable long userId,
                                                                @RequestParam long projectId) {

        User user = userServiceImpl.getUserById(userId);
        Project project = projectServiceImpl.getProjectById(projectId);

        project.addUser(user);

        userServiceImpl.updateUser(user);
        projectServiceImpl.saveProject(project);

        return new ResponseEntity<>(new ProjectDTO(projectId, project.getName(), project.getDescription()), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user) {
        User savedUser = userServiceImpl.saveUser(user);

        return new ResponseEntity<>(new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        //Get user by id
        User user = userServiceImpl.getUserById(id);

        //Delete all connections with projects and tasks before deleting. Because User entity
        //has mappedBy attribute for both tasks and project, therefore we firstly need to delete
        //all connections of a user manually
        user.getProjects()
                .stream()
                .forEach(project -> project.removeUser(id));
        user.getTasks()
                .stream()
                .forEach(task -> task.removeUser(id));

        //Save changes to user and then delete user
        userServiceImpl.updateUser(user);
        userServiceImpl.deleteUser(id);

        return new ResponseEntity<>("User with id "+id+" deleted successfully", HttpStatus.OK);
    }

}
