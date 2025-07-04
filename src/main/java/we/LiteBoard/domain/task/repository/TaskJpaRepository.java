package we.LiteBoard.domain.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.task.entity.Task;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
}
