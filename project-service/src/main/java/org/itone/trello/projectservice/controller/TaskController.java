package org.itone.trello.projectservice.controller;

import org.hibernate.sql.ast.SqlTreeCreationException;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.TaskDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.TaskServiceImpl;
import org.itone.trello.projectservice.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/task")
public class TaskController {

    private final TaskServiceImpl taskServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public TaskController(TaskServiceImpl taskServiceImpl, UserServiceImpl userServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    //TODO: concat strings with StringBuilder or StringBuffer

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {

        Task task = taskServiceImpl.getTaskById(id);
        String boardName = task.getBoard().getName();

        TaskDTO taskDTO = new TaskDTO(task.getId(),
                                      task.getName(),
                                      task.getDescription(),
                                      boardName);

        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/users")
    public ResponseEntity<Set<UserDTO>> getUsersOnTask(@PathVariable long id) {

        Task task = taskServiceImpl.getTaskById(id);

        Set<UserDTO> userDTOs = task.getUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PostMapping("/add/user")
    public ResponseEntity<UserDTO> addUserToTask(@RequestParam long taskId,
                                                 @RequestParam long userId) {

        Task task = taskServiceImpl.getTaskById(taskId);
        User user = userServiceImpl.getUserById(userId);

        task.addUser(user);

        taskServiceImpl.saveTask(task);
        userServiceImpl.updateUser(user);

        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail());

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeTask(@PathVariable long id) {

        taskServiceImpl.deleteTask(id);

        return new ResponseEntity<>("Task with id "+id+" was successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/remove/user")
    public ResponseEntity<String> removeUserFromTask(@RequestParam long taskId,
                                                     @RequestParam long userId) {

        Task task = taskServiceImpl.getTaskById(taskId);
        User user = userServiceImpl.getUserById(userId);

        task.removeUser(userId);

        taskServiceImpl.saveTask(task);
        userServiceImpl.updateUser(user);

        return new ResponseEntity<>("User with id "+userId+" isn't assigned to task with id "+taskId, HttpStatus.OK);
    }
}
