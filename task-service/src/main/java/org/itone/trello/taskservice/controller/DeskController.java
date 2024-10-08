package org.itone.trello.taskservice.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import org.itone.trello.taskservice.dto.BoardDTO;
import org.itone.trello.taskservice.dto.DeskDTO;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;
import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.service.DeskService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("trello/api/v1/desk")
@RequiredArgsConstructor
public class DeskController {

    private final DeskService deskService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


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

    @PostMapping("/save")
    public ResponseEntity<DeskDTO> addDeskToProject(@RequestParam UUID projectId,
                                                    @RequestBody DeskCreationDTO deskCreationDTO) {

        Desk desk = deskService.addDeskToProject(projectId, deskCreationDTO);

        logger.debug("Desk {} was added to project with id {}", desk, projectId);
        return new ResponseEntity<>(desk.toDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDesk(@PathVariable UUID id) {
        deskService.deleteDesk(id);

        logger.debug("Desk with id {} was deleted successfully", id);
    }
}
