package we.LiteBoard.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import we.LiteBoard.domain.member.dto.MemberRequestDTO;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.service.MemberInviteService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원")
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberInviteService inviteService;

    @PostMapping("/invite")
    @Operation(summary = "회원 초대", description = "해당 이메일로 초대 메일을 전송합니다.")
    public SuccessResponse<String> inviteMember(
            @RequestBody @Valid MemberRequestDTO.Invite request,
            @CurrentMember Member inviter
    ) {
        inviteService.invite(request.email(), request.projectId(), inviter);
        return SuccessResponse.ok("초대 메일 전송에 성공했습니다.");
    }
}

