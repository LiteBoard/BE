package we.LiteBoard.domain.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import we.LiteBoard.domain.task.enumerate.Status;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "업무 관련 요청 DTO")
public class TaskRequestDTO {

    @Schema(description = "업무 생성 요청 DTO")
    public record Create(
            @Schema(description = "업무 제목", example = "아키텍처 설계")
            @NotBlank String title,

            @Schema(description = "업무 설명", example = "서비스 개발을 위한 아키텍처 설계 작업")
            String description,

            @Schema(description = "시작일", example = "2025-10-01")
            LocalDate startDate,

            @Schema(description = "마감일", example = "2025-10-02")
            LocalDate endDate,

            @Schema(description = "담당자 ID 리스트", example = "[1, 2]")
            @NotEmpty List<Long> memberIds
    ) {}

    @Schema(description = "업무 수정 요청 DTO")
    public record Update(
            @Schema(description = "업무 제목", example = "아키텍처 설계")
            @NotBlank String title,

            @Schema(description = "업무 설명", example = "예상 비용 측정을 위한 아키텍처 설계 작업")
            String description,

            @Schema(description = "업무 완료 상태 [PENDING/IN_PROGRESS/COMPLETED/DELAYED]", example = "COMPLETED")
            Status status,

            @Schema(description = "시작일", example = "2025-10-01")
            LocalDate startDate,

            @Schema(description = "마감일", example = "2025-10-05")
            LocalDate endDate
    ) {}
}
