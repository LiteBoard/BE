package we.LiteBoard.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.task.entity.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public void updateTitle(String title) {
        this.title = title;
    }

    /** 연관 관계 편의 메서드 */
    public void setProject(Project project) {
        this.project = project;
    }
}
