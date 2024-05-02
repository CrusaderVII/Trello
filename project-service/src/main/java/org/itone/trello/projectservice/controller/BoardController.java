package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dto.*;
import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.service.BoardService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/board")
public class BoardController {

    private final BoardService boardService;

    //Create logger with the name corresponding to BoardController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(BoardController.class);

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
        logger.setLevel(Level.DEBUG);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable long id) {
        Board board = boardService.getBoardById(id);

        BoardDTO boardDTO = new BoardDTO(board.getId(),
                                             board.getName(),
                                             board.getDesk().getName());

        return new ResponseEntity<>(boardDTO, HttpStatus.OK);
    }

    @GetMapping("get/{id}/tasks")
    public ResponseEntity<Set<TaskDTO>> getTasksOnBoard(@PathVariable long id) {
        Board board = boardService.getBoardById(id);

        Set<TaskDTO> taskDTOs = board.getTasks()
                .stream()
                .map(task -> new TaskDTO(task.getId(),
                                             task.getName(),
                                             task.getDescription(),
                                             board.getName()))
                .collect(Collectors.toSet());

        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @PostMapping("add/task")
    public ResponseEntity<TaskDTO> addTaskToBoard(@RequestParam long boardId,
                                                  @RequestBody Task task) {
        //Task can be created only in this (Board) controller
        task = boardService.addTask(boardId, task);
        TaskDTO taskDTO = new TaskDTO(task.getId(),
                                          task.getName(),
                                          task.getDescription(),
                                          task.getBoard().getName());

        logger.debug("New task {} was added to board {}", task, task.getBoard());
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteBoard (@PathVariable long id) {
        boardService.deleteBoard(id);

        logger.debug("Board with id {} was deleted successfully", id);
        return new ResponseEntity<>("Board with "+id+" deleted successfully", HttpStatus.OK);
    }
}
