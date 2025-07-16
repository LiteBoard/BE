package we.LiteBoard.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public class MemberRequestDTO {

    @Schema(description = "회원 초대 요청 DTO")
    public record Invite(
            @Schema(description = "초대 이메일") @Email String email,
            @Schema(description = "초대할 프로젝트 ID") Long projectId
    ) {}
}
