package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.user.InvalidDataException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.UserRepository;
import org.itone.trello.projectservice.service.UserService;
import org.itone.trello.projectservice.service.UserValidationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.encoder = new BCryptPasswordEncoder(5);
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
    public User saveUser (User entity) throws InvalidDataException {
        try {
            //validate data of a new user
            userValidationService.validate(entity);
            //encode password of a new user if validation was successful
            entity.setPassword(encodePassword(entity.getPassword()));
            return userRepository.save(entity);
        } catch (InvalidDataException exception) {
            throw new InvalidDataException(exception.getMessage());
        }
    }

    @Override
    public User updateUser(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteUser (long id) {
        userRepository.deleteById(id);
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
