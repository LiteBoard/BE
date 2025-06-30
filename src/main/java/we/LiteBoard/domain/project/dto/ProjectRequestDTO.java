package we.LiteBoard.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Schema(description = "프로젝트 요청 DTO")
public class ProjectRequestDTO {

    @Schema(description = "프로젝트 생성 요청 DTO")
    public record Create(
            @Schema(description = "프로젝트명", example = "Lite-Board")
            @NotBlank String title,

            @Schema(description = "시작일", example = "2025-09-01")
            @FutureOrPresent LocalDate startDate,

            @Schema(description = "마감일", example = "2026-02-01")
            @FutureOrPresent LocalDate endDate
    ) {}
}
