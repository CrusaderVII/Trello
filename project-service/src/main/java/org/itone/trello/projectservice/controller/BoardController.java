package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.*;
import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.model.*;
import org.itone.trello.projectservice.service.impl.BoardServiceImpl;
import org.itone.trello.projectservice.service.impl.TaskServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/board")
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;
    private final TaskServiceImpl taskServiceImpl;

    //Create logger with the name corresponding to BoardController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(BoardController.class);


    //TODO: study pageable in jpa*
    //TODO: create class Message for responding in delete controllers if needed???
    public BoardController(BoardServiceImpl boardServiceImpl, TaskServiceImpl taskServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
        this.taskServiceImpl = taskServiceImpl;
        logger.setLevel(Level.INFO);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable long id) {
        try {
            Board board = boardServiceImpl.getBoardById(id);

            return new ResponseEntity<>(new BoardDTO(
                    board.getId(),
                    board.getName(),
                    board.getDesk().getName()),
                    HttpStatus.OK);
        } catch (NoSuchBoardException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("get/{id}/tasks")
    public ResponseEntity<Set<TaskDTO>> getTasksOnBoard(@PathVariable long id) {
        try {
            Board board = boardServiceImpl.getBoardById(id);

            Set<TaskDTO> taskDTOs = board.getTasks().stream()
                    .map(task -> new TaskDTO(task.getId(),
                                             task.getName(),
                                             task.getDescription(),
                                             board.getName()))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
        } catch (NoSuchBoardException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("add/task")
    public ResponseEntity<TaskDTO> addTaskToBoard(@RequestParam long boardId,
                                                  @RequestBody Task task) {
        try {
            //Get board by id using boardServiceImpl
            Board board = boardServiceImpl.getBoardById(boardId);

            //Add to set of tasks of gotten board new task. Task can be created only in this (Board) controller.
            //addTask() method also encapsulates setting board of added task to current board, so we don't need
            //to call setBoard() method of task object separately.
            board.addTask(task);

            //Save changes to board and task entities to DB
            taskServiceImpl.saveTask(task);
            boardServiceImpl.saveBoard(board);

            TaskDTO taskDTO = new TaskDTO(task.getId(),
                                          task.getName(),
                                          task.getDescription(),
                                          board.getName());

            logger.info("New task {} was added to board {}", task, board);
            return new ResponseEntity<>(taskDTO, HttpStatus.OK);
        } catch (NoSuchBoardException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteBoard (@PathVariable long id) {
        boardServiceImpl.deleteBoard(id);

        logger.info("Board with id {} was deleted successfully", id);
        return new ResponseEntity<>("Board with "+id+" deleted successfully", HttpStatus.OK);
    }
}
