package we.LiteBoard.domain.memberProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.memberProject.service.MemberProjectService;
import we.LiteBoard.global.common.annotation.ProjectRoleRequired;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로젝트 멤버 관리")
@RequestMapping("/api/v1/projects")
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    @GetMapping("/{projectId}/members")
    @Operation(summary = "프로젝트 멤버 조회", description = "해당 프로젝트에 속한 멤버 리스트를 권한순으로 조회합니다.")
    public SuccessResponse<List<MemberResponseDTO.ProjectMemberResponse>> getProjectMembers(
            @PathVariable Long projectId
    ) {
        List<MemberResponseDTO.ProjectMemberResponse> members = memberProjectService.getProjectMembers(projectId);
        return SuccessResponse.ok(members);
    }

    @PostMapping("/{projectId}/members")
    @ProjectRoleRequired(ProjectRole.ADMIN)
    @Operation(summary = "프로젝트에 멤버 추가", description = "지정한 프로젝트에 멤버를 추가합니다.")
    public SuccessResponse<String> addMember(
            @PathVariable Long projectId,
            @RequestBody @Valid MemberProjectRequestDTO.AddMember request
    ) {
        memberProjectService.addMemberToProject(projectId, request);
        return SuccessResponse.ok("프로젝트에 멤버 추가 성공했습니다.");
    }

    @PatchMapping("/{projectId}/members/role")
    @ProjectRoleRequired(ProjectRole.ADMIN)
    @Operation(summary = "프로젝트 멤버 역할 변경", description = "지정한 멤버의 프로젝트 내 역할을 변경합니다.")
    public SuccessResponse<String> changeMemberRole(
            @PathVariable Long projectId,
            @RequestBody @Valid MemberProjectRequestDTO.ChangeRole request
    ) {
        memberProjectService.changeMemberRole(projectId, request);
        return SuccessResponse.ok("역할이 성공적으로 변경되었습니다.");
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    @ProjectRoleRequired(ProjectRole.ADMIN)
    @Operation(summary = "프로젝트 멤버 제거", description = "지정한 프로젝트에서 멤버를 제거합니다.")
    public SuccessResponse<String> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long memberId
    ) {
        memberProjectService.removeMemberFromProject(projectId, memberId);
        return SuccessResponse.ok("멤버가 프로젝트에서 제거되었습니다.");
    }
}
