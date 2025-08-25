package we.LiteBoard.domain.project.repository;

import we.LiteBoard.domain.project.entity.Project;

import java.util.List;

public interface ProjectRepository {
    Project save(Project project);
    Project getById(Long id);
    List<Project> findAllByMemberId(Long memberId);
    void deleteById(Long id);
}
