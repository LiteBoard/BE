package we.LiteBoard.domain.requestCard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "업무 요청 관련 요청 DTO")
public class RequestCardRequestDTO {

    @Schema(description = "업무 요청 생성 요청 DTO")
    public record Create(
            @Schema(description = "업무 요청 내용", example = "이거 내일까지 해주세요.")
            @NotBlank
            String content,

            @Schema(description = "요청할 Todo 리스트", example = "[\"API 문서 작성\", \"테스트 코드 작성\"]")
            @NotEmpty
            List<String> todoDescriptions
    ) {}

    @Schema(description = "업무 요청 수정 요청 DTO")
    public record Update(
            @Schema(description = "수정할 업무 요청 내용", example = "수정된 업무 요청 내용입니다.")
            @NotBlank
            String content,

            @Schema(description = "수정할 Todo 리스트", example = "[\"수정된 할 일1\", \"수정된 할 일2\"]")
            @NotEmpty
            List<String> todoDescriptions
    ) {}

}
