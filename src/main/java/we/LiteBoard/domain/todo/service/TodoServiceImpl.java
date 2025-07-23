package we.LiteBoard.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.task.repository.TaskRepository;
import we.LiteBoard.domain.todo.dto.TodoRequestDTO;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.domain.todo.repository.TodoRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TaskRepository taskRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * 투두 생성
     * @param taskId TODO가 속할 상위 업무 ID
     * @param request 생성할 내용
     * @return 생성된
     */
    @Override
    @Transactional
    public TodoResponseDTO.Upsert create(Long taskId, TodoRequestDTO.Upsert request) {
        Task task = taskRepository.getById(taskId);

        Member member = null;
        if (request.memberId() != null) {
            member = memberRepository.getById(request.memberId());
        }

        Todo todo = Todo.builder()
                .description(request.description())
                .done(false)
                .task(task)
                .member(member)
                .isRequired(false)
                .build();

        task.getTodos().add(todo);
        task.refreshStatus();

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
    public TodoResponseDTO.Upsert update(Long todoId, TodoRequestDTO.Upsert request, Member currentMember) {
        Todo todo = todoRepository.getById(todoId);
        Member previousMember = todo.getMember();

        Member newMember = previousMember;

        boolean isDifferentMember =
                request.memberId() != null &&
                        (previousMember == null || !Objects.equals(previousMember.getId(), request.memberId()));

        if (isDifferentMember) {
            newMember = memberRepository.getById(request.memberId());
        }

        todo.update(request.description(), newMember);

        // 담당자 변경 시 알림 전송
        if (previousMember == null || !Objects.equals(previousMember.getId(), newMember.getId())) {
            notificationService.notifyTodoAssigned(todo, currentMember);
        }
        return TodoResponseDTO.Upsert.from(todo.getId());
    }

    @Override
    @Transactional
    public List<TodoResponseDTO.Detail> toggleTodos(List<Long> todoIds) {
        Map<Task, Boolean> affectedTasks = new HashMap<>();

        List<TodoResponseDTO.Detail> responses = new ArrayList<>();

        for (Long todoId : todoIds) {
            Todo todo = todoRepository.getById(todoId);
            todo.toggle();
            affectedTasks.put(todo.getTask(), true);
            responses.add(TodoResponseDTO.Detail.from(todo));

            // TODOs 완료 알림
            if (todo.isDone()) {
                notificationService.notifyTodoCompleted(todo);
            }
        }

        for (Task task : affectedTasks.keySet()) {
            boolean changed = task.refreshStatus();

            // 완료 상태가 되면 알림 전송
            if (changed && task.getStatus() == Status.COMPLETED) {
                notificationService.notifyTaskCompleted(task);
            }
        }

        return responses;
    }


    @Override
    @Transactional
    public void deleteById(Long todoId) {
        todoRepository.getById(todoId);
        todoRepository.deleteById(todoId);
    }
}
