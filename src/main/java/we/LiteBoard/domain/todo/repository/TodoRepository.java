package we.LiteBoard.domain.todo.repository;

import we.LiteBoard.domain.todo.entity.Todo;

import java.util.List;

public interface TodoRepository {
    Todo save(Todo todo);
    Todo getById(Long id);
    List<Todo> findAllByTaskId(Long taskId);
    void deleteById(Long id);
    List<Todo> findTodayTodosByMember(Long id);
}
