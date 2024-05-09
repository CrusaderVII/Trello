package org.itone.trello.taskservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dto.*;
import org.itone.trello.taskservice.dto.BoardDTO;
import org.itone.trello.taskservice.dto.creation.BoardCreationDTO;
import org.itone.trello.taskservice.service.BoardService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
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
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable UUID id) {
        Board board = boardService.getBoardById(id);

        return new ResponseEntity<>(board.toDTO(), HttpStatus.OK);
    }

    @GetMapping("/get/{id}/tasks")
    public ResponseEntity<Set<TaskDTO>> getTasksOnBoard(@PathVariable UUID id) {
        Board board = boardService.getBoardById(id);

        Set<TaskDTO> taskDTOs = board.getTasks()
                .stream()
                .map(Task::toDTO)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<BoardDTO> addBoardToDesk(@RequestParam UUID deskId,
                                                   @RequestBody BoardCreationDTO boardCreationDTO) {

        Board board = boardService.addBoardToDesk(deskId, boardCreationDTO);

        logger.debug("New board {} was added to desk {}", board, board.getDesk());
        return new ResponseEntity<>(board.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBoard (@PathVariable UUID id) {
        boardService.deleteBoard(id);

        logger.debug("Board with id {} was deleted successfully", id);
        return new ResponseEntity<>("Board with "+id+" deleted successfully", HttpStatus.OK);
    }
}
