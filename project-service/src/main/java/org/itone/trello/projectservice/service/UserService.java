package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dto.creation.UserCreationDTO;

import java.util.List;
import java.util.UUID;

public interface UserService{
    User getUserById(UUID id);
    User authUser(String email, String rawPassword) ;
    List<User> getAllUsers(int page);
    User saveUser(UserCreationDTO userCreationDTO);
    User updateUserPassword(UserCreationDTO userCreationDTO, String newPassword);
    User updateUser(User entity);
    void deleteUser(UUID id);
}
