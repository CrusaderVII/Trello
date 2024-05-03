package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService{
    User getUserById(UUID id);
    User authUser(String email, String rawPassword) ;
    List<User> getAllUsers();
    User saveUser(User entity);
    User updateUserPassword(User user, String newPassword);
    User updateUser(User entity);
    void deleteUser(UUID id);
}
