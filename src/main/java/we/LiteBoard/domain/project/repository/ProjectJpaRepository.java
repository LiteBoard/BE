package we.LiteBoard.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.project.entity.Project;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
}
