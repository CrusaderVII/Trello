package org.itone.trello.projectservice.dao;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.repository.BoardRepository;
import org.itone.trello.projectservice.dto.creation.BoardCreationDTO;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class BoardDAO {

    private final BoardRepository boardRepository;
    private final DeskDAO deskDAO;

    public BoardDAO(BoardRepository boardRepository, DeskDAO deskDAO) {
        this.boardRepository = boardRepository;
        this.deskDAO = deskDAO;
    }

    public Board findById(UUID id) throws NoSuchBoardException {
        return boardRepository.findById(id)
                .orElseThrow(() ->new NoSuchBoardException("id "+id));
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

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

    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }

}
