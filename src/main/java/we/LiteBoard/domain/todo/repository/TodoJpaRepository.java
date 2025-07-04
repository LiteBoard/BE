package we.LiteBoard.domain.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.todo.entity.Todo;

public interface TodoJpaRepository extends JpaRepository<Todo, Long> {
}
