package org.itone.trello.projectservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.service.DeskService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;
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
        logger.setLevel(Level.DEBUG);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DeskDTO> getDeskById(@PathVariable UUID id) {
        Desk desk = deskService.getDeskById(id);

        return new ResponseEntity<>(desk.toDTO(), HttpStatus.OK);
    }
    @GetMapping("/get/{deskId}/boards")
    public ResponseEntity<Set<BoardDTO>> getAllBoards(@PathVariable UUID deskId) {
        Desk desk = deskService.getDeskById(deskId);

        Set<BoardDTO> boardDTOs = desk.getBoards()
                .stream()
                .map(Board::toDTO)
                .collect(Collectors.toSet());

            return new ResponseEntity<>(boardDTOs, HttpStatus.OK);
    }

    @PostMapping("add/board")
    public ResponseEntity<BoardDTO> addBoardToDesk(@RequestParam UUID deskId,
                                                   @RequestBody Board board) {
        //Board can be created only in this (Desk) controller
        board = deskService.addBoardToDesk(deskId, board);

        logger.debug("New board {} was added to desk {}", board, board.getDesk());
        return new ResponseEntity<>(board.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDesk(@PathVariable UUID id) {
        deskService.deleteDesk(id);

        logger.debug("Desk with id {} was deleted successfully", id);
        return new ResponseEntity<>("Desk with id "+id+" deleted successfully", HttpStatus.OK);
    }
}
