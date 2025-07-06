package we.LiteBoard.domain.requestCardTodo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.requestCard.repository.RequestCardRepository;
import we.LiteBoard.domain.requestCardTodo.entity.RequestCardTodo;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.domain.todo.repository.TodoRepository;
import we.LiteBoard.domain.todo.util.factory.TodoFactory;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestCardTodoServiceImpl implements RequestCardTodoService {

    private final RequestCardRepository requestCardRepository;
    private final TodoRepository todoRepository;

    @Override
    @Transactional
    public Long acceptTodo(Long requestCardId, Long requestCardTodoId, Member currentMember) {
        RequestCard requestCard = requestCardRepository.getById(requestCardId);

        // 요청 메시지에 속한 Todos 중 해당 ID 찾기
        RequestCardTodo matched = requestCard.getTodos().stream()
                .filter(todo -> todo.getId().equals(requestCardTodoId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_CARD_TODO_NOT_FOUND));

        Todo todo = TodoFactory.fromRequestCardTodo(matched, requestCard.getTask(), currentMember);
        todoRepository.save(todo);

        return todo.getId();
    }
}
