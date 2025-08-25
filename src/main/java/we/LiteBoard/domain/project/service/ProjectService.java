package we.LiteBoard.domain.project.service;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.project.dto.ProjectRequestDTO;
import we.LiteBoard.domain.project.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {
    Long create(Member currentMember, ProjectRequestDTO.Create request);
    ProjectResponseDTO.Detail getById(Long projectId);
    List<ProjectResponseDTO.Summary> getAllByMember(Member currentMember);
    void delete(Long projectId);
}
