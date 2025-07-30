package we.LiteBoard.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.memberProject.entity.MemberProject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberProject> memberProjects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    /** 연관 관계 편의 메서드 */
    public void addMemberProject(MemberProject memberProject) {
        this.memberProjects.add(memberProject);
        memberProject.assignProject(this);
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.assignProject(this);
    }
}
