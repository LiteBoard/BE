package we.LiteBoard.domain.taskMember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.taskMember.entity.TaskMember;

public interface TaskMemberRepository extends JpaRepository<TaskMember, Long> {
}
