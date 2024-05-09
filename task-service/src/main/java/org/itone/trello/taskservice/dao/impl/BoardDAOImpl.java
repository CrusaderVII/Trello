package org.itone.trello.taskservice.dao.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.taskservice.dao.BoardDAO;
import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dao.model.Desk;
import org.itone.trello.taskservice.dao.repository.BoardRepository;
import org.itone.trello.taskservice.dto.creation.BoardCreationDTO;
import org.itone.trello.taskservice.util.exception.board.NoSuchBoardException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class BoardDAOImpl implements BoardDAO {

    private final BoardRepository boardRepository;
    private final DeskDAOImpl deskDAO;

    public BoardDAOImpl(BoardRepository boardRepository, DeskDAOImpl deskDAO) {
        this.boardRepository = boardRepository;
        this.deskDAO = deskDAO;
    }

    @Override
    public Board findById(UUID id) throws NoSuchBoardException {
        return boardRepository.findById(id)
                .orElseThrow(() ->new NoSuchBoardException("id "+id));
    }

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO) {
        Desk desk = deskDAO.findById(deskId);
        Board board = Board.fromCreationDTO(boardCreationDTO);

        //Add to set of boards of gotten desk new board.
        //addBoard() method also encapsulates setting desk of added board to current desk, so we don't need
        //to call setDesk() method of board object separately.
        desk.addBoard(board);

        deskDAO.save(desk);
        return save(board);
    }

    @Override
    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }

}
