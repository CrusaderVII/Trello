package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.User;

import java.util.List;

public interface UserService{
    User getUserById(long id);
    User authUser(String email, String rawPassword) ;
    List<User> getAllUsers();
    User saveUser(User entity);
    User updateUserPassword(User user, String newPassword);
    User updateUser(User entity);
    void deleteUser(long id);
}
