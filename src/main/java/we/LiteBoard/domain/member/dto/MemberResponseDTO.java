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

    @Schema(description = "멤버 조회 응답 DTO")
    public record Detail(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이름") String name,
            @Schema(description = "프로필 사진 주소") String profileUrl
    ) {
        public static Detail from(Member member) {
            return new Detail(
                    member.getId(),
                    member.getName(),
                    member.getPicture()
            );
        }
    }

    @Schema(description = "내 정보 응답 DTO")
    public record MyInfo(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이름") String name,
            @Schema(description = "알림 수신 여부") boolean notificationEnabled
    ) {
        public static MyInfo from(Member member) {
            return new MyInfo(
                    member.getId(),
                    member.getName(),
                    member.isNotificationEnabled()
            );
        }
    }
}
