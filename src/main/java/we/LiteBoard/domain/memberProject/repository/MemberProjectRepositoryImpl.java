package we.LiteBoard.domain.memberProject.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberProjectRepositoryImpl implements MemberProjectRepository {

    private final MemberProjectJpaRepository memberProjectJpaRepository;

    @Override
    public boolean existsByProjectAndMember(Long projectId, Long memberId) {
        return memberProjectJpaRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }
}
