package we.LiteBoard.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.project.dto.ProjectRequestDTO;
import we.LiteBoard.domain.project.dto.ProjectResponseDTO;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;

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
    public Long create(ProjectRequestDTO.Create request) {
        Project project = Project.builder()
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

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
}
