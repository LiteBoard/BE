package we.LiteBoard.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.repository.TaskRepository;
import we.LiteBoard.domain.todo.dto.TodoRequestDTO;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.domain.todo.repository.TodoRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TaskRepository taskRepository;

    /**
     * 투두 생성
     * @param currentMember 요청 보내는 회원 - 업무 담당자
     * @param taskId TODO가 속할 상위 업무 ID
     * @param request 생성할 내용
     * @return 생성된
     */
    @Override
    @Transactional
    public TodoResponseDTO.Upsert create(Member currentMember, Long taskId, TodoRequestDTO.Upsert request) {
        Task task = taskRepository.getById(taskId);

        Todo todo = Todo.builder()
                .description(request.description())
                .done(false)
                .task(task)
                .member(currentMember)
                .build();

        return TodoResponseDTO.Upsert.from(todoRepository.save(todo).getId());
    }

    @Override
    public List<TodoResponseDTO.Detail> getAllByTask(Long taskId) {
        return todoRepository.findAllByTaskId(taskId).stream()
                .map(TodoResponseDTO.Detail::from)
                .toList();
    }

    @Override
    @Transactional
    public TodoResponseDTO.Upsert update(Long todoId, TodoRequestDTO.Upsert request) {
        Todo todo = todoRepository.getById(todoId);
        todo.updateDescription(request.description());
        return TodoResponseDTO.Upsert.from(todo.getId());
    }

    @Override
    @Transactional
    public List<TodoResponseDTO.Detail> toggleTodos(List<Long> todoIds) {
        return todoIds.stream()
                .map(todoRepository::getById)
                .peek(Todo::toggle)
                .map(TodoResponseDTO.Detail::from)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}
