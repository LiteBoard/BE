package we.LiteBoard.domain.memberProject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;
import we.LiteBoard.domain.memberProject.service.MemberProjectService;
import we.LiteBoard.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로젝트 멤버 관리")
@RequestMapping("/api/v1/projects")
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    @PostMapping("/{projectId}/members")
    @Operation(summary = "프로젝트에 멤버 추가", description = "지정한 프로젝트에 멤버를 추가합니다.")
    public SuccessResponse<String> addMember(
            @PathVariable Long projectId,
            @RequestBody @Valid MemberProjectRequestDTO.AddMember request
    ) {
        memberProjectService.addMemberToProject(projectId, request);
        return SuccessResponse.ok("프로젝트에 멤버 추가 성공했습니다.");
    }

}
