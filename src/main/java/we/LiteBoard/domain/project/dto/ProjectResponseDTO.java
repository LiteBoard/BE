package we.LiteBoard.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.project.entity.Project;

import java.time.LocalDate;

@Schema(description = "프로젝트 관련 응답 DTO")
public class ProjectResponseDTO {

    @Schema(description = "프로젝트 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "프로젝트 ID") Long id,
            @Schema(description = "시작일") LocalDate startDate,
            @Schema(description = "마감일") LocalDate endDate
    ) {
        public static Detail from(Project project) {
            return new Detail(
                    project.getId(),
                    project.getStartDate(),
                    project.getEndDate()
            );
        }
    }
}
