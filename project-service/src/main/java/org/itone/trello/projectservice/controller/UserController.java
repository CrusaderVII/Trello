package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trello/api/v1/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    //TODO: implement adding user to project ???
    //TODO: list all projects of user
    //TODO: auth controller
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
        userServiceImpl.deleteUser(id);

        return new ResponseEntity<>("User with id "+id+" deleted successfully", HttpStatus.OK);
    }

}
