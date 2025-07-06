package we.LiteBoard.domain.requestCard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "업무 요청 관련 요청 DTO")
public class RequestCardRequestDTO {

    @Schema(description = "업무 요청 생성 요청 DTO")
    public record Create(
            @Schema(description = "업무 요청 내용", example = "이거 내일까지 해주세요.")
            String content,

            @Schema(description = "요청할 Todo 리스트", example = "[\"API 문서 작성\", \"테스트 코드 작성\"]")
            List<String> todoDescriptions
    ) {}
}
