package org.itone.trello.projectservice.controller;

import org.itone.trello.projectservice.dto.BoardDTO;
import org.itone.trello.projectservice.dto.DeskDTO;
import org.itone.trello.projectservice.dto.ProjectDTO;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.service.impl.BoardServiceImpl;
import org.itone.trello.projectservice.service.impl.DeskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("trello/api/v1/desk")
public class DeskController {

    private final DeskServiceImpl deskServiceImpl;
    private final BoardServiceImpl boardServiceImpl;

    public DeskController(DeskServiceImpl deskServiceImpl, BoardServiceImpl boardServiceImpl) {
        this.deskServiceImpl = deskServiceImpl;
        this.boardServiceImpl = boardServiceImpl;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DeskDTO> getDeskById(@PathVariable long id) {
        Desk desk = deskServiceImpl.getDeskById(id);
        ProjectDTO projectDTO = new ProjectDTO(desk.getProject().getId(),
                                               desk.getProject().getName(),
                                               desk.getProject().getDescription());

        return new ResponseEntity<>(new DeskDTO(
                desk.getId(),
                desk.getName(),
                projectDTO),
                HttpStatus.OK);
    }
//    @GetMapping("/get/all")
//    public ResponseEntity<List<DeskDTO>> getAllDesks() {
//        List<Desk> desks = deskServiceImpl.getAllDesks();
//        ProjectDTO projectDTO = new ProjectDTO(desks.get(0).getProject().getId(),
//                                               desks.get(0).getProject().getName(),
//                                               desks.get(0).getProject().getDescription());
//
//        List<DeskDTO> deskDTOs = desks.stream()
//                .map(desk -> new DeskDTO(desk.getId(), desk.getName(), projectDTO))
//                .toList();
//
//        return new ResponseEntity<>(deskDTOs, HttpStatus.OK);
//    }

    //TODO: create controller to see all boards of this desk
    @PostMapping("add/board")
    public ResponseEntity<BoardDTO> addUserToProject(@RequestParam long deskId,
                                                     @RequestBody Board board) {
        //Get desk by id using deskServiceImpl
        Desk desk = deskServiceImpl.getDeskById(deskId);

        //Add to set of boards of gotten desk new board. Board can be created only in this (Desk) controller.
        //addBoard() method also encapsulates setting desk of added board to current desk, so we don't need
        //to call setDesk() method of board object separately.
        desk.addBoard(board);

        //Save changes to desk and board entities to DB
        boardServiceImpl.saveBoard(board);
        deskServiceImpl.saveDesk(desk);

        return new ResponseEntity<>(new BoardDTO(board.getId(), board.getName(), desk.getName()), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDesk(@PathVariable long id) {
        deskServiceImpl.deleteDesk(id);

        return new ResponseEntity<String>("Desk with id "+id+" deleted successfully", HttpStatus.OK);
    }
}
