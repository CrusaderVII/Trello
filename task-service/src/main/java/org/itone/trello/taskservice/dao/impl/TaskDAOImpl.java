package org.itone.trello.taskservice.dao.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.taskservice.dao.TaskDAO;
import org.itone.trello.taskservice.dao.model.Board;
import org.itone.trello.taskservice.dao.model.Task;
import org.itone.trello.taskservice.dao.model.User;
import org.itone.trello.taskservice.dao.repository.TaskRepository;
import org.itone.trello.taskservice.dto.creation.TaskCreationDTO;
import org.itone.trello.taskservice.util.exception.board.NoSuchBoardException;
import org.itone.trello.taskservice.util.exception.task.NoSuchTaskException;
import org.itone.trello.taskservice.util.exception.user.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class TaskDAOImpl implements TaskDAO {

    private final TaskRepository taskRepository;
    private final UserDAOImpl userDAO;
    private final BoardDAOImpl boardDAO;

    public TaskDAOImpl(TaskRepository taskRepository, UserDAOImpl userDAO, BoardDAOImpl boardDAO) {
        this.taskRepository = taskRepository;
        this.userDAO = userDAO;
        this.boardDAO = boardDAO;
    }

    @Override
    public Task findById(UUID id) throws NoSuchTaskException {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskException("id "+id));
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task addTaskToBoard(UUID boardId, TaskCreationDTO taskCreationDTO) throws NoSuchBoardException {
        Board board = boardDAO.findById(boardId);
        Task task = Task.fromCreationDTO(taskCreationDTO);

        //Add to set of tasks of gotten board new task.
        //addTask() method also encapsulates setting board of added task to current board, so we don't need
        //to call setBoard() method of task object separately.
        board.addTask(task);

        boardDAO.save(board);
        return save(task);
    }

    @Override
    public User addUserToTask(UUID taskId, UUID userId) throws NoSuchTaskException, NoSuchUserException {
        Task task = findById(taskId);
        User user = userDAO.findById(userId);

        task.addUser(user);

        save(task);
        return userDAO.save(user);
    }

    @Override
    public Task changeBoard(UUID taskId, UUID newBoardId) throws NoSuchTaskException, NoSuchBoardException {
        Task task = findById(taskId);

        //Get old board of task from gotten task and get new board using newBoardId
        Board oldBoard = task.getBoard();
        Board newBoard = boardDAO.findById(newBoardId);

        //Remove task from old board and add then add task to new board by calling addTask() method.
        //Then use addTask() on a new board. This method also encapsulate setting board of added task to current board
        oldBoard.removeTask(task);
        newBoard.addTask(task);

        boardDAO.save(oldBoard);
        boardDAO.save(newBoard);
        return save(task);
    }

    @Override
    public void removeUserFromTask(UUID taskId, UUID userId) throws NoSuchTaskException, NoSuchUserException {
        Task task = findById(taskId);
        User user = userDAO.findById(userId);

        //removeUser() method also encapsulate removing this task from set of tasks of given user
        task.removeUser(userId);

        save(task);
        userDAO.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        taskRepository.deleteById(id);
    }
}
