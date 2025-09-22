package we.LiteBoard.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;

import java.util.List;

@Schema(description = "회원 관련 요청 DTO")
public class MemberRequestDTO {

    @Schema(description = "성명 수정 DTO")
    public record UpdateName(
            @Schema(description = "수정할 성명", example = "김재관")
            @NotBlank String nickName
    ) {}

    @Schema(description = "회원 대량 초대 요청 DTO")
    public record InviteBulk(
            @Schema(description = "초대 이메일 목록", example = "[\"a@naver.com\",\"b@gmail.com\"]")
            @NotEmpty @Size(max = 20)
            List<@Email String> emails,

            @Schema(description = "초대할 프로젝트 ID", example = "1")
            @NotNull Long projectId,

            @Schema(description = "권한", example = "VIEWER")
            @NotNull ProjectRole role
    ) {}
}
