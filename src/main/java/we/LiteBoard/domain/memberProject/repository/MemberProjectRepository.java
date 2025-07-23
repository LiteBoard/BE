package we.LiteBoard.domain.memberProject.repository;

import we.LiteBoard.domain.memberProject.entity.MemberProject;

import java.util.Optional;

public interface MemberProjectRepository {
    boolean existsByProjectAndMember(Long projectId, Long memberIdLong);
    Optional<MemberProject> getMemberProjectById(Long projectId, Long memberId);
    void delete(MemberProject memberProject);
}
