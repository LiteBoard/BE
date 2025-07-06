package we.LiteBoard.domain.memberProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;

@Schema(description = "프로젝트 멤버 요청 DTO")
public class MemberProjectRequestDTO {

    @Schema(description = "프로젝트 멤버 추가 요청 DTO")
    public record AddMember(
            @Schema(description = "추가할 멤버 ID", example = "1")
            @NotNull Long memberId,

            @Schema(description = "역할", example = "VIEWER")
            @NotNull ProjectRole projectRole
    ) {}
}
