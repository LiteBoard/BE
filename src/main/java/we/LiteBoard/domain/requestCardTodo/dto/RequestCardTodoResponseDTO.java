package we.LiteBoard.domain.requestCardTodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.requestCardTodo.entity.RequestCardTodo;

@Schema(description = "업무 요청 TODO 관련 응답 DTO")
public class RequestCardTodoResponseDTO {

    @Schema(description = "업무 요청 TODO 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "업무 요청 TODO ID") Long id,
            @Schema(description = "내용") String description
    ) {
        public static RequestCardTodoResponseDTO.Detail from(RequestCardTodo requestCardTodo) {
            return new RequestCardTodoResponseDTO.Detail(
                    requestCardTodo.getId(),
                    requestCardTodo.getDescription()
            );
        }
    }
}
