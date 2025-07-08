package we.LiteBoard.domain.task.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.todo.entity.Todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TASK")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestCard> requestCards = new ArrayList<>();

    public void update(String title, String description, Status status, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 업무 상태 갱신 메서드 */
    public void refreshStatus() {
        if (todos.isEmpty()) {
            this.status = Status.IN_PROGRESS;
            return;
        }

        boolean allDone = todos.stream().allMatch(Todo::isDone);
        if (allDone) {
            this.status = Status.COMPLETED;
            return;
        }

        if (this.endDate != null && LocalDate.now().isBefore(this.endDate.plusDays(1))) {
            this.status = Status.IN_PROGRESS;
        } else {
            this.status = Status.DELAYED;
        }
    }
}
