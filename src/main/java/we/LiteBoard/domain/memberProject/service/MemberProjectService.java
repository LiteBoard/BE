package we.LiteBoard.domain.memberProject.service;

import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;

import java.util.List;

public interface MemberProjectService {
    List<MemberResponseDTO.ProjectMemberResponse> getProjectMembers(Long projectId);
    void addMemberToProject(Long projectId, MemberProjectRequestDTO.AddMember request);
    void changeMemberRole(Long projectId, MemberProjectRequestDTO.ChangeRole request);
    void removeMemberFromProject(Long projectId, Long memberId);
}
