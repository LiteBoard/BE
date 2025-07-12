package we.LiteBoard.domain.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.todo.entity.Todo;

@Schema(description = "TODO 관련 응답 DTO")
public class TodoResponseDTO {

    @Schema(description = "TODO 생성 응답 DTO")
    public record Upsert(
            @Schema(description = "TODO ID") Long id
    ) {
        public static TodoResponseDTO.Upsert from(Long todoId) {
            return new TodoResponseDTO.Upsert(
                    todoId
            );
        }
    }

    @Schema(description = "TODO 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "TODO ID") Long id,
            @Schema(description = "담당자") MemberResponseDTO.Detail member,
            @Schema(description = "내용") String description,
            @Schema(description = "완료 여부") Boolean done
    ) {
        public static TodoResponseDTO.Detail from(Todo todo) {
            return new TodoResponseDTO.Detail(
                    todo.getId(),
                    todo.getMember() != null ? MemberResponseDTO.Detail.from(todo.getMember()) : null,
                    todo.getDescription(),
                    todo.isDone()
            );
        }
    }
}
