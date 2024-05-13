package org.itone.trello.taskservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.taskservice.dto.TaskDTO;
import org.itone.trello.taskservice.dto.UserDTO;
import org.itone.trello.taskservice.dto.creation.TaskCreationDTO;
import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.service.TaskService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
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
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);

        return new ResponseEntity<>(task.toDTO(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}/users")
    public ResponseEntity<Set<UserDTO>> getUsersOnTask(@PathVariable UUID id) {
        Task task = taskService.getTaskById(id);

        Set<UserDTO> userDTOs = task.getUsers().stream()
                .map(User::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<TaskDTO> addTaskToBoard(@RequestParam UUID boardId,
                                                  @RequestBody TaskCreationDTO taskCreationDTO) {

        Task task = taskService.addTaskToBoard(boardId, taskCreationDTO);

        logger.debug("New task {} was added to board {}", task, task.getBoard());
        return new ResponseEntity<>(task.toDTO(), HttpStatus.OK);
    }

    @PostMapping("/add/user")
    public ResponseEntity<UserDTO> addUserToTask(@RequestParam UUID taskId,
                                                 @RequestParam UUID userId) {

        User user = taskService.addUserToTask(taskId, userId);

        logger.debug("User {} was assigned to task with id {}", user, taskId);
        return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
    }

    @PutMapping("/change/{taskId}/board")
    public ResponseEntity<TaskDTO> changeBoard(@PathVariable UUID taskId,
                                               @RequestParam UUID newBoardId) {

        Task task = taskService.changeBoard(taskId, newBoardId);

        logger.debug("Task {} was moved to board with id {}", task, newBoardId);
        return new ResponseEntity<>(task.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);

        logger.debug("Task with id {} was deleted successfully", id);
    }

    @PutMapping("/remove/user")
    public void removeUserFromTask(@RequestParam UUID taskId,
                                                     @RequestParam UUID userId) {
        taskService.removeUserFromTask(taskId, userId);

        logger.debug("User with id {} was removed from task with id {}", userId, taskId);
        //TODO: Think, what this controller can return
    }
}
