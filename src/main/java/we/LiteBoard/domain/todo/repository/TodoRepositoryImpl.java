package we.LiteBoard.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

import static we.LiteBoard.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository {

    private final TodoJpaRepository todoJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Todo save(Todo todo) {
        return todoJpaRepository.save(todo);
    }

    @Override
    public Todo getById(Long id) {
        return todoJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
    }

    @Override
    public List<Todo> findAllByTaskId(Long taskId) {
        return queryFactory
                .selectFrom(todo)
                .where(todo.task.id.eq(taskId))
                .fetch();
    }

    @Override
    public void deleteById(Long id) {
        todoJpaRepository.deleteById(id);
    }
}
