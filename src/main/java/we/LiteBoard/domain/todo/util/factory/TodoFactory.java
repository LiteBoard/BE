package we.LiteBoard.domain.todo.util.factory;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCardTodo.entity.RequestCardTodo;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.todo.entity.Todo;

public class TodoFactory {
    public static Todo fromRequestCardTodo(RequestCardTodo requestCardTodo, Task task, Member member) {
        return Todo.builder()
                .description(requestCardTodo.getDescription())
                .done(false)
                .isRequired(true)
                .task(task)
                .member(member)
                .build();
    }
}
