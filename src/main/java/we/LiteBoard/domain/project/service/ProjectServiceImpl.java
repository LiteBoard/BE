package we.LiteBoard.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.project.dto.ProjectRequestDTO;
import we.LiteBoard.domain.project.dto.ProjectResponseDTO;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * 프로젝트 생성
     * @param request 생성 요청 DTO
     * @return 생성된 프로젝트 ID
     */
    @Override
    @Transactional
    public Long create(Member currentMember, ProjectRequestDTO.Create request) {
        Project project = Project.builder()
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        MemberProject memberProject = MemberProject.builder()
                .projectRole(ProjectRole.ADMIN)
                .build();

        project.addMemberProject(memberProject);
        currentMember.addMemberProject(memberProject);

        return projectRepository.save(project).getId();
    }

    /**
     * 프로젝트 ID로 단건 조회
     * @param projectId 조회할 프로젝트 ID
     * @return 조회된 프로젝트 정보
     */
    @Override
    public ProjectResponseDTO.Detail getById(Long projectId) {
        Project project = projectRepository.getById(projectId);
        return ProjectResponseDTO.Detail.from(project);
    }

    /**
     * 현재 로그인 중인 유저의 프로젝트 리스트 조회
     * @param currentMember 현재 로그인 중인 유저
     * @return 프로젝트 리스트
     */
    @Override
    public List<ProjectResponseDTO.Summary> getAllByMember(Member currentMember) {
        return projectRepository.findAllByMemberId(currentMember.getId())
                .stream()
                .map(ProjectResponseDTO.Summary::from)
                .toList();
    }

    /**
     * 프로젝트 삭제
     * @param projectId 삭제할 프로젝트 ID
     */
    @Override
    @Transactional
    public void delete(Long projectId) {
        projectRepository.getById(projectId);
        projectRepository.deleteById(projectId);
    }
}
