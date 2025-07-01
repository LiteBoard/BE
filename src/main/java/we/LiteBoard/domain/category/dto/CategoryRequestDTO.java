package we.LiteBoard.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "카테고리 관련 요청 DTO")
public class CategoryRequestDTO {

    @Schema(description = "카테고리 생성/수정 요청 DTO")
    public record Upsert(
            @Schema(description = "카테고리명", example = "설계")
            @NotBlank String title
    ) {}
}
