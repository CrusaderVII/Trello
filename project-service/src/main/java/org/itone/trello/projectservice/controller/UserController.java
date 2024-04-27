package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    //TODO: implement adding user to project ???
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

        //Call userServiceImpl, that encapsulates finding user by email and then
        //using BCryptPasswordEncoder check if passwords match. In case wrong email or password
        //exception will be thrown
        User user = userServiceImpl.authUser(email, password);
        UserDTO userDTO = new UserDTO(user.getId(),
                                      user.getName(),
                                      user.getEmail());

        return new ResponseEntity<>(userDTO, HttpStatus.OK);

    }

    //TODO: InvalidDataException handler. What is right way to implement it???
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
        User user = userServiceImpl.getUserById(id);

        user.getProjects()
                .stream()
                .forEach(project -> project.removeUser(id));
        user.getTasks()
                .stream()
                .forEach(task -> task.removeUser(id));

        userServiceImpl.updateUser(user);
        userServiceImpl.deleteUser(id);

        return new ResponseEntity<>("User with id "+id+" deleted successfully", HttpStatus.OK);
    }

}
