package we.LiteBoard.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Schema(description = "회원 관련 요청 DTO")
public class MemberRequestDTO {

    @Schema(description = "성명 수정 DTO")
    public record UpdateName(
            @Schema(description = "수정할 성명", example = "김재관")
            @NotBlank String nickName
    ) {}

    @Schema(description = "회원 초대 요청 DTO")
    public record Invite(
            @Schema(description = "초대 이메일") @Email String email,
            @Schema(description = "초대할 프로젝트 ID") Long projectId
    ) {}
}
