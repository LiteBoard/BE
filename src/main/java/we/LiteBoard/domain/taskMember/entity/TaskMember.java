package we.LiteBoard.domain.taskMember.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.task.entity.Task;

@Entity
@Table(name = "TASK_MEMBER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TASK_MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void assignTask(Task task) {
        this.task = task;
        task.getTaskMembers().add(this);
    }

    public void assignMember(Member member) {
        this.member = member;
        member.getTaskMembers().add(this);
    }
}

