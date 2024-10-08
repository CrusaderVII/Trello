package org.itone.trello.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.itone.trello.taskservice.dao.model.Project;
import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dto.AuthDTO;
import org.itone.trello.taskservice.dto.ProjectDTO;
import org.itone.trello.taskservice.dto.TaskDTO;
import org.itone.trello.taskservice.dto.UserDTO;
import org.itone.trello.taskservice.dto.creation.UserCreationDTO;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.qos.logback.classic.*;


import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
    }
    @GetMapping("/get/all/{page}")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PathVariable int page) {
        List<User> users = userService.getAllUsers(page);

        List<UserDTO> userDTOs = users.stream()
                .map(User::toDTO)
                .toList();

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/projects")
    public ResponseEntity<Set<ProjectDTO>> getProjectsOfUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        Set<ProjectDTO> projectDTOs = user.getProjects()
                .stream()
                .map(Project::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/tasks")
    public ResponseEntity<Set<TaskDTO>> getTasksOfUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);

        Set<TaskDTO> taskDTOs = user.getTasks()
                .stream()
                .map(Task::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<UserDTO> authUser(@RequestBody AuthDTO authDTO) {
        //Call userServiceImpl, that encapsulates finding user by email and then
        //using BCryptPasswordEncoder check if passwords match. In case wrong email or password
        //exception will be thrown
        User user = userService.authUser(authDTO);

        return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserCreationDTO userCreationDTO) {
        User savedUser = userService.saveUser(userCreationDTO);

        logger.debug("New user {} was added", savedUser);
        return new ResponseEntity<>(savedUser.toDTO(), HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<UserDTO> updateUserPassword(@RequestBody AuthDTO authDTO,
                                                      @RequestParam String newPassword) {

        User userWithNewPassword = userService.updateUserPassword(authDTO, newPassword);

        logger.debug("User {} changed the password successfully", userWithNewPassword);
        return new ResponseEntity<>(userWithNewPassword.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        logger.debug("User with id {} was successfully deleted", id);
    }

}
