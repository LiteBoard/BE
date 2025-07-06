package we.LiteBoard.domain.memberProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.memberProject.entity.MemberProject;

public interface MemberProjectJpaRepository extends JpaRepository<MemberProject, Long> {
    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
