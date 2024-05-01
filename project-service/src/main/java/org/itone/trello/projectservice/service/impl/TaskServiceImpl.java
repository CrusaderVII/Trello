package org.itone.trello.projectservice.service.impl;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.exception.board.NoSuchBoardException;
import org.itone.trello.projectservice.exception.task.NoSuchTaskException;
import org.itone.trello.projectservice.exception.user.NoSuchUserException;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.TaskRepository;
import org.itone.trello.projectservice.repository.UserRepository;
import org.itone.trello.projectservice.service.BoardService;
import org.itone.trello.projectservice.service.TaskService;
import org.itone.trello.projectservice.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final BoardService boardService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, BoardService boardService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.boardService = boardService;
    }

    @Override
    public Task getTaskById(long id) throws NoSuchTaskException{
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskException("id "+id));
    }

    @Override
    public Task saveTask(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public User addUserToTask(long taskId, long userId) throws NoSuchTaskException, NoSuchUserException {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);

        task.addUser(user);

        saveTask(task);
        return userService.updateUser(user);
    }

    @Override
    public Task changeBoard(long taskId, long newBoardId) throws NoSuchTaskException, NoSuchBoardException {
        Task task = getTaskById(taskId);

        //Get old board of task from gotten task and get new board using newBoardId
        Board oldBoard = task.getBoard();
        Board newBoard = boardService.getBoardById(newBoardId);

        //Remove task from old board and add then add task to new board by calling addTask() method.
        //This method also encapsulate setting board of added task to current board
        oldBoard.removeTask(task);
        newBoard.addTask(task);

        boardService.saveBoard(oldBoard);
        boardService.saveBoard(newBoard);
        return saveTask(task);
    }

    @Override
    public void removeUserFromTask(long taskId, long userId) throws NoSuchTaskException, NoSuchUserException {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);

        //removeUser() method also encapsulate removing this task from set of tasks of given user
        task.removeUser(userId);

        saveTask(task);
        userService.updateUser(user);
    }

    @Override
    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }
}
