package we.LiteBoard.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.entity.Member;

@Schema(description = "멤버 관련 응답 DTO")
public class MemberResponseDTO {

    @Schema(description = "멤버 요약 응답 DTO")
    public record Summary(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이메일") String email
    ) {
        public static MemberResponseDTO.Summary from(Member member) {
            return new MemberResponseDTO.Summary(
                    member.getId(),
                    member.getEmail()
            );
        }
    }

    @Schema(description = "멤버 이름 응답 DTO")
    public record Detail(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이름") String name
    ) {
        public static Detail from(Member member) {
            return new Detail(
                    member.getId(),
                    member.getName()
            );
        }
    }
}
