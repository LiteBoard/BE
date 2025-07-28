package we.LiteBoard.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.dto.MemberRequestDTO;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.service.MemberService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "멤버")
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/my")
    @Operation(summary = "내 정보 조회", description = "현재 사용자의 성명, 이메일, 알림 수신 여부를 조회합니다.")
    public SuccessResponse<MemberResponseDTO.MyInfo> getMyInfo(
            @CurrentMember Member member
    ) {
        return SuccessResponse.ok(MemberResponseDTO.MyInfo.from(member));
    }

    @PatchMapping("/my/notification")
    @Operation(summary = "알림 수신 여부 변경", description = "해당 사용자의 알림 수신 여부를 변경합니다.")
    public SuccessResponse<MemberResponseDTO.MyInfo> toggleNotification(
            @CurrentMember Member currentMember
    ) {
        return SuccessResponse.ok(memberService.toggleNoticeStatus(currentMember));
    }

    @PatchMapping("/my")
    @Operation(summary = "성명 수정", description = "해당 사용자의 성명을 수정합니다.")
    public SuccessResponse<MemberResponseDTO.MyInfo> changeNickname(
            @RequestBody @Valid MemberRequestDTO.UpdateName request,
            @CurrentMember Member member
    ){
        return SuccessResponse.ok(memberService.changeNickName(request, member));
    }
}
