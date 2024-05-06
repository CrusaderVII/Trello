package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.BoardDAO;
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
public class BoardServiceImpl implements BoardService {

    private final BoardDAO boardDAO;

    public BoardServiceImpl(BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
    }

    @Override
    public Board getBoardById(UUID id) throws NoSuchBoardException{
        return boardDAO.findById(id);
    }

    @Override
    public Board updateBoard(Board board) {
        return boardDAO.save(board);
    }

    @Override
    public Board addBoardToDesk(UUID deskId, BoardCreationDTO boardCreationDTO) {
        return boardDAO.addBoardToDesk(deskId, boardCreationDTO);
    }

    @Override
    public void deleteBoard(UUID id) {
        boardDAO.deleteById(id);
    }
}
