package we.LiteBoard.domain.memberProject.repository;

import we.LiteBoard.domain.memberProject.entity.MemberProject;

import java.util.List;
import java.util.Optional;

public interface MemberProjectRepository {
    List<MemberProject> findAllByProjectId(Long projectId);
    boolean existsByProjectAndMember(Long projectId, Long memberIdLong);
    Optional<MemberProject> getMemberProjectById(Long projectId, Long memberId);
    void delete(MemberProject memberProject);
}
