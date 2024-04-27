package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.repository.BoardRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board getBoardById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() ->new NoSuchBoardException("id "+id));
    }

    @Override
    public Board saveBoard(Board entity) {
        return boardRepository.save(entity);
    }

    @Override
    public void deleteBoard(long id) {
        boardRepository.deleteById(id);
    }
}
