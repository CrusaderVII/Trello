package org.itone.trello.projectservice.controller;

import org.hibernate.sql.ast.SqlTreeCreationException;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.dto.TaskDTO;
import org.itone.trello.projectservice.dto.UserDTO;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.service.impl.BoardServiceImpl;
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
    private final BoardServiceImpl boardServiceImpl;

    public TaskController(TaskServiceImpl taskServiceImpl,
                          UserServiceImpl userServiceImpl,
                          BoardServiceImpl boardServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.boardServiceImpl = boardServiceImpl;
    }

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

    @PostMapping("/change/{taskId}/board")
    public ResponseEntity<TaskDTO> changeBoard(@PathVariable long taskId,
                                               @RequestParam long newBoardId) {

        Task task = taskServiceImpl.getTaskById(taskId);

        //Get old board of task from gotten task and get new board using newBoardId
        Board oldBoard = task.getBoard();
        Board newBoard = boardServiceImpl.getBoardById(newBoardId);

        //Remove task from old board and add then add task to new board by calling addTask() method.
        //This method also encapsulate setting board of added task to current board
        oldBoard.removeTask(task);
        newBoard.addTask(task);

        //Save changes to DB
        taskServiceImpl.saveTask(task);
        boardServiceImpl.saveBoard(oldBoard);
        boardServiceImpl.saveBoard(newBoard);

        return new ResponseEntity<>(new TaskDTO(task.getId(),
                                                task.getName(),
                                                task.getDescription(),
                                                newBoard.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable long id) {

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
