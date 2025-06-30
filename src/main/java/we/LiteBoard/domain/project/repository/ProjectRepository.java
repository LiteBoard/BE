package we.LiteBoard.domain.project.repository;

import we.LiteBoard.domain.project.entity.Project;

public interface ProjectRepository {
    Project save(Project project);
    Project getById(Long id);
    void deleteById(Long id);
}
