package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.UserRepository;
import org.itone.trello.projectservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("id "+id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser (User entity) {
        return userRepository.save(entity);
    }
    @Override
    public void deleteUser (long id) {
        userRepository.deleteById(id);
    }
}
