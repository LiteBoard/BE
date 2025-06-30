package we.LiteBoard.domain.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.project.entity.Project;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "프로젝트 관련 응답 DTO")
public class ProjectResponseDTO {

    @Schema(description = "프로젝트 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "프로젝트 ID") Long id,
            @Schema(description = "시작일") LocalDate startDate,
            @Schema(description = "마감일") LocalDate endDate,
            @Schema(description = "프로젝트 멤버 목록") List<MemberSummary> members
    ) {
        public static Detail from(Project project) {
            List<MemberSummary> memberDTOs = project.getMemberProjects().stream()
                    .map(MemberSummary::from)
                    .toList();

            return new Detail(
                    project.getId(),
                    project.getStartDate(),
                    project.getEndDate(),
                    memberDTOs
            );
        }
    }

    @Schema(description = "프로젝트 멤버 요약 DTO")
    public record MemberSummary(
            @Schema(description = "멤버 ID") Long id,
            @Schema(description = "이메일") String email,
            @Schema(description = "프로젝트 역할") ProjectRole projectRole
    ) {
        public static MemberSummary from(MemberProject mp) {
            return new MemberSummary(
                    mp.getMember().getId(),
                    mp.getMember().getEmail(),
                    mp.getProjectRole()
            );
        }
    }
}
