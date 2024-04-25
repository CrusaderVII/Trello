package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.model.User;

import java.util.List;

public interface UserService{
    User getUserById(long id);
    List<User> getAllUsers();
    User saveUser(User entity);
    void deleteUser(long id);
}
