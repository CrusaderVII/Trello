package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.exception.project.NoSuchProjectException;
import org.itone.trello.projectservice.exception.user.InvalidDataException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.exception.user.WrongPasswordException;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.UserService;
import org.itone.trello.projectservice.service.impl.ProjectServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ch.qos.logback.classic.*;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/user")
public class UserController {

    private final UserService userService;

    //Create logger with the name corresponding to UserController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;

        //Set effective (means real level, including influence of parent root logger) level of logger to INFO,
        //to allow only INFO, WARN, ERROR log requests
        logger.setLevel(Level.INFO);
    }

    //TODO: Create controller for changing email

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {
        try {
            User user = userService.getUserById(id);

            UserDTO userDTO = new UserDTO(user.getId(),
                                          user.getName(),
                                          user.getEmail());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getId(),
                                         user.getName(),
                                         user.getEmail()))
                .toList();

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/projects")
    public ResponseEntity<Set<ProjectDTO>> getProjectsOfUser(@PathVariable long id) {
        try {
            User user = userService.getUserById(id);

            Set<ProjectDTO> projectDTOs = user.getProjects()
                    .stream()
                    .map( project -> new ProjectDTO(project.getId(),
                                                    project.getName(),
                                                    project.getDescription()))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(projectDTOs, HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<UserDTO> authUser(@RequestParam String email,
                                            @RequestParam String password) {
        try {
            //Call userServiceImpl, that encapsulates finding user by email and then
            //using BCryptPasswordEncoder check if passwords match. In case wrong email or password
            //exception will be thrown
            User user = userService.authUser(email, password);

            UserDTO userDTO = new UserDTO(user.getId(),
                                          user.getName(),
                                          user.getEmail());

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (WrongPasswordException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);

            UserDTO userDTO = new UserDTO(savedUser.getId(),
                                          savedUser.getName(),
                                          savedUser.getEmail());

            logger.info("New user {} added", savedUser);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (InvalidDataException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/password")
    public ResponseEntity<UserDTO> updateUserPassword(@RequestBody User userFromRequest,
                                                      @RequestParam String newPassword) {
        try {
            User userWithNewPassword = userService.updateUserPassword(userFromRequest, newPassword);

            UserDTO userDTO = new UserDTO(userWithNewPassword.getId(),
                                          userWithNewPassword.getName(),
                                          userWithNewPassword.getEmail());

            logger.info("User {} changed the password successfully", userWithNewPassword);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (WrongPasswordException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (InvalidDataException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        try {
            userService.deleteUser(id);

            logger.info("User with id {} was successfully deleted", id);
            return new ResponseEntity<>("User with id "+id+" deleted successfully", HttpStatus.OK);
        } catch (NoSuchUserException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
