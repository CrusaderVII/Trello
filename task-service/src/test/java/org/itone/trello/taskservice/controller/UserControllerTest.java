package org.itone.trello.taskservice.controller;

import org.itone.trello.taskservice.dao.UserDAO;
import org.itone.trello.taskservice.dao.impl.UserDAOImpl;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.dto.UserDTO;
import org.itone.trello.taskservice.dto.creation.UserCreationDTO;
import org.itone.trello.taskservice.service.UserService;
import org.itone.trello.taskservice.service.UserValidationService;
import org.itone.trello.taskservice.service.impl.UserServiceImpl;
import org.itone.trello.taskservice.util.exception.user.InvalidDataException;
import org.itone.trello.taskservice.util.exception.user.InvalidEmailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    UserDAO userDAO = Mockito.mock(UserDAOImpl.class);
    UserService userService = new UserServiceImpl(userDAO, new UserValidationService());
    UserController controller = new UserController(userService);

    @Test
    void saveUser_RequestIsValid_ReturnsSavedUser() {
        //Create test case
        UserCreationDTO user = new UserCreationDTO("TEST", "test@yandex.ru", "TESTTEST1");
        UUID id = UUID.randomUUID();
        User userFromDAO = new User("TEST", "test@yandex.ru", "TESTTEST1");
        userFromDAO.setId(id);

        //Set behavior to mocked userDAO
        when(userDAO.save(User.fromUserCreationDTO(user)))
                .thenReturn(userFromDAO);

        //Get result from calling controller and create expected result
        UserDTO savedUser = controller.saveUser(user).getBody();
        UserDTO rightUser = new UserDTO(id, "TEST", "test@yandex.ru");

        //Assert that actual result equals expected result
        assertEquals(rightUser, savedUser);
    }

    @Test
    void saveUserWithInvalidEmail_RequestIsInvalid_ReturnsInvalidDataException() {
        UserCreationDTO user = new UserCreationDTO("TEST", "testyandex.ru", "TESTTEST1");
        UUID id = UUID.randomUUID();
        User userFromDAO = new User("TEST", "test@yandex.ru", "TESTTEST1");
        userFromDAO.setId(id);

        when(userDAO.save(User.fromUserCreationDTO(user)))
                .thenReturn(userFromDAO);

        //Assert that saveUser() method will throw expected exception
        Assertions.assertThrows(InvalidDataException.class, () -> controller.saveUser(user));
    }

    @Test
    void saveUserWithInvalidName_RequestIsInvalid_ReturnsInvalidDataException() {
        UserCreationDTO user = new UserCreationDTO("", "test@yandex.ru", "TESTTEST1");
        UUID id = UUID.randomUUID();
        User userFromDAO = new User("TEST", "test@yandex.ru", "TESTTEST1");
        userFromDAO.setId(id);

        when(userDAO.save(User.fromUserCreationDTO(user)))
                .thenReturn(userFromDAO);

        Assertions.assertThrows(InvalidDataException.class, () -> controller.saveUser(user));
    }

    @Test
    void saveUserWithInvalidPassword_RequestIsInvalid_ReturnsInvalidDataException() {
        UserCreationDTO user = new UserCreationDTO("TEST", "test@yandex.ru", "T");
        UUID id = UUID.randomUUID();
        User userFromDAO = new User("TEST", "test@yandex.ru", "TESTTEST1");
        userFromDAO.setId(id);

        when(userDAO.save(User.fromUserCreationDTO(user)))
                .thenReturn(userFromDAO);

        Assertions.assertThrows(InvalidDataException.class, () -> controller.saveUser(user));
    }
}