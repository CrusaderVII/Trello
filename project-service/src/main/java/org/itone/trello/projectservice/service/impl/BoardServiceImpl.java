package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dto.creation.BoardCreationDTO;
import org.itone.trello.projectservice.service.DeskService;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.repository.BoardRepository;
import org.itone.trello.projectservice.dao.repository.TaskRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final DeskService deskService;

    public BoardServiceImpl(BoardRepository boardRepository, DeskService deskService) {
        this.boardRepository = boardRepository;
        this.deskService = deskService;
    }

    @Override
    public Board getBoardById(UUID id) throws NoSuchBoardException{
        return boardRepository.findById(id)
                .orElseThrow(() ->new NoSuchBoardException("id "+id));
    }

    @Override
    public Board updateBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO) {
        Desk desk = deskService.getDeskById(deskId);
        Board board = Board.fromCreationDTO(boardCreationDTO);

        //Add to set of boards of gotten desk new board.
        //addBoard() method also encapsulates setting desk of added board to current desk, so we don't need
        //to call setDesk() method of board object separately.
        desk.addBoard(board);

        deskService.updateDesk(desk);
        return updateBoard(board);
    }

    @Override
    public void deleteBoard(UUID id) {
        boardRepository.deleteById(id);
    }
}
