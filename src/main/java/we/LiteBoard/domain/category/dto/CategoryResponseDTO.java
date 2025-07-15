package we.LiteBoard.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;

import java.util.List;

@Schema(description = "카테고리 관련 응답 DTO")
public class CategoryResponseDTO {

    @Schema(description = "카테고리 생성 응답 DTO")
    public record Upsert(
            @Schema(description = "카테고리 ID") Long id
    ) {
        public static CategoryResponseDTO.Upsert from(Long categoryId) {
            return new CategoryResponseDTO.Upsert(
                    categoryId
            );
        }
    }

    @Schema(description = "카테고리 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "카테고리 ID") Long id,
            @Schema(description = "카테고리명") String title,
            @Schema(description = "카테고리에 속한 업무 목록") List<TaskResponseDTO.Summary> tasks

    ) {
        public static CategoryResponseDTO.Detail from(Category category) {
            List<TaskResponseDTO.Summary> taskSummaries = category.getTasks().stream()
                    .map(TaskResponseDTO.Summary::from)
                    .toList();

            return new CategoryResponseDTO.Detail(
                    category.getId(),
                    category.getTitle(),
                    taskSummaries
            );
        }
    }
}
