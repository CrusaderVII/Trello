package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.repository.BoardRepository;
import org.itone.trello.projectservice.dao.repository.TaskRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;

    public BoardServiceImpl(BoardRepository boardRepository, TaskRepository taskRepository) {
        this.boardRepository = boardRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Board getBoardById(long id) throws NoSuchBoardException{
        return boardRepository.findById(id)
                .orElseThrow(() ->new NoSuchBoardException("id "+id));
    }

    @Override
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Task addTask(long boardId, Task task) {
        //Add to set of tasks of gotten board new task.
        //addTask() method also encapsulates setting board of added task to current board, so we don't need
        //to call setBoard() method of task object separately.
        Board board = getBoardById(boardId);
        board.addTask(task);

        saveBoard(board);
        return taskRepository.save(task);
    }

    @Override
    public void deleteBoard(long id) {
        boardRepository.deleteById(id);
    }
}
