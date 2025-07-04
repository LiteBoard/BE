package we.LiteBoard.domain.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "TODO 관련 요청 DTO")
public class TodoRequestDTO {

    @Schema(description = "TODO 생성/수정 요청 DTO")
    public record Upsert(
            @Schema(description = "TODO 내용", example = "시스템 아키텍처 설계")
            @NotBlank String description
    ) {}

    @Schema(description = "TODO 토글 상태 변경 요청 DTO")
    public record Toggle(
            @Schema(description = "상태 변경할 TODO ID 리스트", example = "[1]") List<Long> todoIds
    ) {}
}
