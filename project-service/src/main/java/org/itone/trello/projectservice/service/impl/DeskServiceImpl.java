package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.util.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.repository.DeskRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.itone.trello.projectservice.service.DeskService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class DeskServiceImpl implements DeskService {

    private final DeskRepository deskRepository;
    private final BoardService boardService;

    public DeskServiceImpl(DeskRepository deskRepository, BoardService boardService) {
        this.deskRepository = deskRepository;
        this.boardService = boardService;
    }

    @Override
    public Desk getDeskById(UUID id) throws NoSuchDeskException{
        return deskRepository.findById(id)
                .orElseThrow(() -> new NoSuchDeskException("id "+id));
    }

    public Board addBoardToDesk(UUID deskId, Board board) {
        //Add to set of boards of gotten desk new board.
        //addBoard() method also encapsulates setting desk of added board to current desk, so we don't need
        //to call setDesk() method of board object separately.
        Desk desk = getDeskById(deskId);
        desk.addBoard(board);

        saveDesk(desk);
        return boardService.saveBoard(board);
    }

    @Override
    public Desk saveDesk(Desk entity) {
        return deskRepository.save(entity);
    }

    @Override
    public void deleteDesk(UUID id) {
        deskRepository.deleteById(id);
    }
}
