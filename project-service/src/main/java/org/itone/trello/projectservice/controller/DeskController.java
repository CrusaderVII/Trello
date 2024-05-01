package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.service.DeskService;
import org.itone.trello.projectservice.service.impl.BoardServiceImpl;
import org.itone.trello.projectservice.service.impl.DeskServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/desk")
public class DeskController {

    private final DeskService deskService;

    //Create logger with the name corresponding to DeskController from Logback using LoggerFactory from Sel4j,
    //because Logback implements Sel4j interfaces.
    private final Logger logger = (Logger) LoggerFactory.getLogger(DeskController.class);

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
        logger.setLevel(Level.INFO);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DeskDTO> getDeskById(@PathVariable long id) {
        try {
            Desk desk = deskService.getDeskById(id);

            DeskDTO deskDTO = new DeskDTO(desk.getId(),
                                          desk.getName(),
                                          desk.getProject().getName());

            return new ResponseEntity<>(deskDTO, HttpStatus.OK);
        } catch (NoSuchDeskException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/get/{deskId}/boards")
    public ResponseEntity<Set<BoardDTO>> getAllBoards(@PathVariable long deskId) {
        try {
            Desk desk = deskService.getDeskById(deskId);

            Set<BoardDTO> boardDTOs = desk.getBoards()
                    .stream()
                    .map(board -> new BoardDTO(board.getId(),
                                               board.getName(),
                                               desk.getName()))
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
        } catch (NoSuchDeskException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("add/board")
    public ResponseEntity<BoardDTO> addBoardToDesk(@RequestParam long deskId,
                                                   @RequestBody Board board) {
        try {
            //Board can be created only in this (Desk) controller
            board = deskService.addBoardToDesk(deskId, board);

            BoardDTO boardDTO = new BoardDTO(board.getId(), board.getName(), board.getDesk().getName());

            logger.info("New board {} was added to desk {}", board, board.getDesk());
            return new ResponseEntity<>(boardDTO, HttpStatus.OK);
        } catch (NoSuchDeskException exc) {
            logger.warn(exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDesk(@PathVariable long id) {
        deskService.deleteDesk(id);

        logger.info("Desk with id {} was deleted successfully", id);
        return new ResponseEntity<>("Desk with id "+id+" deleted successfully", HttpStatus.OK);
    }
}
