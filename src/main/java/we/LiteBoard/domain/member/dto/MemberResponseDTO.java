package we.LiteBoard.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.entity.Member;

@Schema(description = "멤버 관련 응답 DTO")
public class MemberResponseDTO {

    @Schema(description = "멤버 조회 응답 DTO")
    public record Detail(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이름") String nickname,
            @Schema(description = "프로필 사진 주소") String profileUrl
    ) {
        public static Detail from(Member member) {
            return new Detail(
                    member.getId(),
                    member.getNickname(),
                    member.getPicture()
            );
        }
    }

    @Schema(description = "내 정보 응답 DTO")
    public record MyInfo(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이름") String nickname,
            @Schema(description = "알림 수신 여부") boolean notificationEnabled
    ) {
        public static MyInfo from(Member member) {
            return new MyInfo(
                    member.getId(),
                    member.getNickname(),
                    member.isNotificationEnabled()
            );
        }
    }

    @Schema(description = "토큰 발급시 반환하는 멤버 정보 응답 DTO")
    public record Info (
        @Schema(description = "멤버 ID") Long id,
        @Schema(description = "이름") String nickname,
        @Schema(description = "프로필 사진 주소") String profileUrl
    ) {
        public static Info from(Member member) {
            return new Info(
                    member.getId(),
                    member.getNickname(),
                    member.getPicture()
            );
        }
    }
}
