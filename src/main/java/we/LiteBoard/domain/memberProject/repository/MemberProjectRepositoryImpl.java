package we.LiteBoard.domain.memberProject.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;

import java.util.List;
import java.util.Optional;

import static we.LiteBoard.domain.memberProject.entity.QMemberProject.memberProject;

@Repository
@RequiredArgsConstructor
public class MemberProjectRepositoryImpl implements MemberProjectRepository {

    private final MemberProjectJpaRepository memberProjectJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberProject> findAllByProjectId(Long projectId) {
        NumberExpression<Integer> roleOrder = new CaseBuilder()
                .when(memberProject.projectRole.eq(ProjectRole.ADMIN)).then(1)
                .when(memberProject.projectRole.eq(ProjectRole.EDITOR)).then(2)
                .when(memberProject.projectRole.eq(ProjectRole.VIEWER)).then(3)
                .otherwise(4);

        return queryFactory
                .selectFrom(memberProject)
                .join(memberProject.member).fetchJoin()
                .where(memberProject.project.id.eq(projectId))
                .orderBy(roleOrder.asc())
                .fetch();
    }

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
