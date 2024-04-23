package org.itone.trello.authservice.controller;

import lombok.*;
import org.itone.trello.authservice.model.User;
import org.itone.trello.authservice.repository.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("trello/auth/v1/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/get")
    public User userAuth(@RequestParam String login, @RequestParam String password) {
        return userService.getUserByLoginAndPassword(login, password);
    }

    @PostMapping("/save")
    public User saveUser(@RequestBody User newUser) {
        return userService.saveUser(newUser);
    }

}
