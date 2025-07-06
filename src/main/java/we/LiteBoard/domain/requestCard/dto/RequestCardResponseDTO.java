package we.LiteBoard.domain.requestCard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.requestCardTodo.dto.RequestCardTodoResponseDTO;

import java.util.List;

@Schema(description = "업무 요청 관련 응답 DTO")
public class RequestCardResponseDTO {

    @Schema(description = "업무 요청 생성 응답 DTO")
    public record Upsert(
            @Schema(description = "업무 요청 ID") Long id
    ) {
        public static RequestCardResponseDTO.Upsert from(RequestCard requestCard) {
            return new RequestCardResponseDTO.Upsert(
                    requestCard.getId()
            );
        }
    }

    @Schema(description = "업무 요청 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "업무 요청 ID") Long id,
            @Schema(description = "내용") String content,
            @Schema(description = "요청자") MemberResponseDTO.Summary sender,
            @Schema(description = "수신자") MemberResponseDTO.Summary receiver,
            @Schema(description = "요청 Todo") List<RequestCardTodoResponseDTO.Detail> todos
    ) {
        public static RequestCardResponseDTO.Detail from(RequestCard requestCard) {
            return new RequestCardResponseDTO.Detail(
                    requestCard.getId(),
                    requestCard.getContent(),
                    MemberResponseDTO.Summary.from(requestCard.getSender()),
                    MemberResponseDTO.Summary.from(requestCard.getReceiver()),
                    requestCard.getTodos().stream()
                            .map(RequestCardTodoResponseDTO.Detail::from)
                            .toList()
            );
        }
    }
}
