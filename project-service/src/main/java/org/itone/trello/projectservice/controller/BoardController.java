package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.*;
import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.model.*;
import org.itone.trello.projectservice.service.impl.BoardServiceImpl;
import org.itone.trello.projectservice.service.impl.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trello/api/v1/board")
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;
    private final TaskServiceImpl taskServiceImpl;

    public BoardController(BoardServiceImpl boardServiceImpl, TaskServiceImpl taskServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
        this.taskServiceImpl = taskServiceImpl;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable long id) {
        Board board = boardServiceImpl.getBoardById(id);
        Desk desk = boardServiceImpl.getDeskOfBoard(id);

        return new ResponseEntity<>(new BoardDTO(
                board.getId(),
                board.getName(),
                desk.getName()),
                HttpStatus.OK);
    }

    @GetMapping("get/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksOnBoard(@PathVariable long id) {
        Board board = boardServiceImpl.getBoardById(id);
        List<Task> tasks = boardServiceImpl.getAllTasksOnBoard(id);

        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> new TaskDTO(task.getId(),
                                         task.getName(),
                                         task.getDescription(),
                                         board.getName()))
                .toList();

        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @PostMapping("add/task")
    public ResponseEntity<TaskDTO> addTaskToBoard(@RequestParam long boardId,
                                                  @RequestBody Task task) {
        //Get board by id using boardServiceImpl
        Board board = boardServiceImpl.getBoardById(boardId);

        //Add to set of tasks of gotten board new task. Task can be created only in this (Board) controller.
        //addTask() method also encapsulates setting board of added task to current board, so we don't need
        //to call setBoard() method of task object separately.
        board.addTask(task);

        //Save changes to board and task entities to DB
        taskServiceImpl.saveTask(task);
        boardServiceImpl.saveBoard(board);

        return new ResponseEntity<>(new TaskDTO(task.getId(),
                                                task.getName(),
                                                task.getDescription(),
                                                board.getName()), HttpStatus.OK);

    }
}
