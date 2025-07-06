package we.LiteBoard.domain.requestCardTodo.service;

import we.LiteBoard.domain.member.entity.Member;

public interface RequestCardTodoService {

    Long acceptTodo(Long requestCardId, Long todoId, Member currentMember);
}
