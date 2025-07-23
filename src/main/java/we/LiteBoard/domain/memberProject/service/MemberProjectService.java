package we.LiteBoard.domain.memberProject.service;

import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;

public interface MemberProjectService {
    void addMemberToProject(Long projectId, MemberProjectRequestDTO.AddMember request);
    void changeMemberRole(Long projectId, MemberProjectRequestDTO.ChangeRole request);
    void removeMemberFromProject(Long projectId, Long memberId);
}
