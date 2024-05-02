package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.TaskDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.util.exception.task.NoSuchTaskException;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.service.TaskService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    //Create logger with the name corresponding to TaskController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        logger.setLevel(Level.DEBUG);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {
        Task task = taskService.getTaskById(id);

        TaskDTO taskDTO = new TaskDTO(task.getId(),
                                          task.getName(),
                                          task.getDescription(),
                                          task.getBoard().getName());

        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @GetMapping("/get/{id}/users")
    public ResponseEntity<Set<UserDTO>> getUsersOnTask(@PathVariable long id) {
        Task task = taskService.getTaskById(id);

        Set<UserDTO> userDTOs = task.getUsers().stream()
                .map(user -> new UserDTO(user.getId(),
                                             user.getName(),
                                             user.getEmail()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PostMapping("/add/user")
    public ResponseEntity<UserDTO> addUserToTask(@RequestParam long taskId,
                                                 @RequestParam long userId) {

        User user = taskService.addUserToTask(taskId, userId);

        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail());

        logger.debug("User {} was assigned to task with id {}", user, taskId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/change/{taskId}/board")
    public ResponseEntity<TaskDTO> changeBoard(@PathVariable long taskId,
                                               @RequestParam long newBoardId) {

        Task task = taskService.changeBoard(taskId, newBoardId);

        TaskDTO taskDTO = new TaskDTO(task.getId(),
                                          task.getName(),
                                          task.getDescription(),
                                          task.getBoard().getName());

        logger.debug("Task {} was moved to board with id {}", task, newBoardId);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);

        logger.debug("Task with id {} was deleted successfully", id);
        return new ResponseEntity<>("Task with id "+id+" was successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/remove/user")
    public ResponseEntity<String> removeUserFromTask(@RequestParam long taskId,
                                                     @RequestParam long userId) {
        taskService.removeUserFromTask(taskId, userId);

        logger.debug("User with id {} was removed from task with id {}", userId, taskId);
        return new ResponseEntity<>("User with id "+userId+" isn't assigned to task with id "+taskId, HttpStatus.OK);
    }
}
