package we.LiteBoard.domain.memberProject.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.project.entity.Project;

@Entity
@Table(name = "MEMBER_PROJECT")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberProject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_PROJECT_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROJECT_ROLE")
    private ProjectRole projectRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    /** 연관 관계 편의 메서드 */
    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
