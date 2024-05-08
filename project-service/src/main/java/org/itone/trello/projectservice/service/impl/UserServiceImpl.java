package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.dao.UserDAO;
import org.itone.trello.projectservice.dao.impl.UserDAOImpl;
import org.itone.trello.projectservice.dto.AuthDTO;
import org.itone.trello.projectservice.dto.creation.UserCreationDTO;
import org.itone.trello.projectservice.util.exception.user.EmailAlreadyExistsException;
import org.itone.trello.projectservice.util.exception.user.InvalidDataException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.util.exception.user.WrongPasswordException;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.service.UserService;
import org.itone.trello.projectservice.service.UserValidationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserValidationService userValidationService;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserDAO userDAO, UserValidationService userValidationService) {
        this.userDAO = userDAO;
        this.userValidationService = userValidationService;
        this.encoder = new BCryptPasswordEncoder(5);
    }

    @Override
    public User getUserById(UUID id) throws NoSuchUserException{
        return userDAO.findById(id);
    }

    @Override
    public User authUser(AuthDTO authDTO) throws NoSuchUserException, WrongPasswordException{
        User user = userDAO.findByEmail(authDTO.email());

        if (encoder.matches(authDTO.password(), user.getPassword())) return user;
        else throw new WrongPasswordException();
    }

    //Page contains data about 20 users
    @Override
    public List<User> getAllUsers(int page) {
        return userDAO.findAll(page);
    }

    @Override
    public User saveUser (UserCreationDTO userCreationDTO) throws InvalidDataException, EmailAlreadyExistsException {
        try {
            //Create new user object from gotten userCreationDTO object from request
            User user = User.fromUserCreationDTO(userCreationDTO);

            //validate data of a new user
            userValidationService.validate(user);

            //encode password of a new user if validation was successful
            user.setPassword(encodePassword(user.getPassword()));
            return userDAO.save(user);
        } catch (InvalidDataException exc) {
            throw new InvalidDataException(exc.getMessage());
        } catch (DataIntegrityViolationException exc) {
            throw new EmailAlreadyExistsException(userCreationDTO.email());
        }
    }

    @Override
    public User updateUserPassword(AuthDTO authDTO, String newPassword) throws NoSuchUserException,
            WrongPasswordException, InvalidDataException {
        //Check if user requesting password change is not some criminal
        User user = authUser(authDTO);

        //Set new password to user
        user.setPassword(newPassword);

        //Check if new password is valid
        userValidationService.validate(user);

        //Set to user encoded password before saving changes to DB
        user.setPassword(encodePassword(newPassword));
        return userDAO.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userDAO.save(user);
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
        userDAO.deleteById(id);
    }

    private String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
