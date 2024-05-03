package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.util.exception.user.InvalidDataException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.util.exception.user.WrongPasswordException;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dao.repository.UserRepository;
import org.itone.trello.projectservice.service.UserService;
import org.itone.trello.projectservice.service.UserValidationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidationService userValidationService;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.encoder = new BCryptPasswordEncoder(5);
    }

    //TODO: add throws declaration to all getById methods in all serviceImpls
    @Override
    public User getUserById(UUID id) throws NoSuchUserException{
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("id "+id));
    }

    @Override
    public User authUser(String email, String rawPassword) throws NoSuchUserException, WrongPasswordException{
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserException("email "+email));

        if (encoder.matches(rawPassword, user.getPassword())) return user;
        else throw new WrongPasswordException();
    }

    //Page contains data about 20 users
    @Override
    public List<User> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return userRepository.findAllUsers(pageable)
                .stream()
                .toList();
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
    public User updateUserPassword(User userFromRequest, String newPassword) throws NoSuchUserException,
            WrongPasswordException, InvalidDataException{
        //Check if user requesting password change is not some criminal
        User user = authUser(userFromRequest.getEmail(), userFromRequest.getPassword());

        //Set new password to user and then use .saveUser() method, because it encapsulates data validation
        user.setPassword(newPassword);
        return saveUser(user);
    }

    @Override
    public User updateUser(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteUser (UUID id) {
        //Get user by id
        User user = getUserById(id);

        //Delete all connections with projects and tasks before deleting. Because User entity
        //has mappedBy attribute for both tasks and project, therefore we firstly need to delete
        //all connections of a user manually
        user.getProjects()
                .stream()
                .forEach(project -> project.removeUser(id));
        user.getTasks()
                .stream()
                .forEach(task -> task.removeUser(id));

        //Save changes to user and then delete user
        updateUser(user);
        userRepository.deleteById(id);
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
