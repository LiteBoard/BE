package we.LiteBoard.domain.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

import static we.LiteBoard.domain.member.entity.QMember.member;
import static we.LiteBoard.domain.memberProject.entity.QMemberProject.memberProject;
import static we.LiteBoard.domain.project.entity.QProject.project;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Project getById(Long id) {
        return projectJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    @Override
    public List<Project> findAllByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(project)
                .distinct()
                .join(project.memberProjects, memberProject)
                .join(memberProject.member, member)
                .where(member.id.eq(memberId))
                .orderBy(project.id.desc())
                .fetch();
    }

    @Override
    public void deleteById(Long id) {
        projectJpaRepository.deleteById(id);
    }
}
