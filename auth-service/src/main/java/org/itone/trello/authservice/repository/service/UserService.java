package org.itone.trello.authservice.repository.service;

import org.itone.trello.authservice.model.User;
import org.itone.trello.authservice.repository.UserRepository;
import org.itone.trello.authservice.repository.exception.NoSuchUserException;
import org.itone.trello.authservice.repository.exception.WrongPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DataValidationService dataValidationService;

    public User getUserByLoginAndPassword(String login, String password) throws NoSuchUserException{
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchUserException(login));

        if (!user.getPassword().equals(password)) throw new WrongPasswordException();

        return user;
    }

    public User saveUser(User user) {
        dataValidationService.validateEmail(user.getEmail());
        dataValidationService.validatePassword(user.getPassword());

        return userRepository.save(user);
    }
}
