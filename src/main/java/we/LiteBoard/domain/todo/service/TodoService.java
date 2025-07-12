package we.LiteBoard.domain.todo.service;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.todo.dto.TodoRequestDTO;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;

import java.util.List;

public interface TodoService {
    TodoResponseDTO.Upsert create(Long taskId, TodoRequestDTO.Upsert request);
    List<TodoResponseDTO.Detail> getAllByTask(Long taskId);
    TodoResponseDTO.Upsert update(Long todoId, TodoRequestDTO.Upsert request, Member currentMember);
    List<TodoResponseDTO.Detail> toggleTodos(List<Long> longs);
    void deleteById(Long todoId);
}
