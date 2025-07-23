package we.LiteBoard.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PatchMapping("/me/notification")
    @Operation(summary = "알림 수신 여부 변경", description = "해당 사용자의 알림 수신 여부를 변경합니다.")
    public SuccessResponse<MemberResponseDTO.MyInfo> toggleNotification(
            @CurrentMember Member currentMember
    ) {
        return SuccessResponse.ok(memberService.toggleNoticeStatus(currentMember));
    }
}
