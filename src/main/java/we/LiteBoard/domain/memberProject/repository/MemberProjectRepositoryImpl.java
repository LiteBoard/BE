package we.LiteBoard.domain.memberProject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.memberProject.entity.MemberProject;

import java.util.Optional;

import static we.LiteBoard.domain.memberProject.entity.QMemberProject.memberProject;

@Repository
@RequiredArgsConstructor
public class MemberProjectRepositoryImpl implements MemberProjectRepository {

    private final MemberProjectJpaRepository memberProjectJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByProjectAndMember(Long projectId, Long memberId) {
        return memberProjectJpaRepository.existsByProjectIdAndMemberId(projectId, memberId);
    }

    @Override
    public Optional<MemberProject> getMemberProjectById(Long projectId, Long memberId) {
        MemberProject result = queryFactory
                .selectFrom(memberProject)
                .where(
                        memberProject.project.id.eq(projectId),
                        memberProject.member.id.eq(memberId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public void delete(MemberProject memberProject) {
        memberProjectJpaRepository.delete(memberProject);
    }
}
