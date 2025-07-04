package we.LiteBoard.domain.requestCard.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCardTodo.entity.RequestCardTodo;
import we.LiteBoard.domain.task.entity.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REQUEST_CARD")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_CARD_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID", nullable = false)
    private Task task;

    @Builder.Default
    @OneToMany(mappedBy = "requestCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestCardTodo> todos = new ArrayList<>();

    /** 연관 관계 편의 메서드 */
    public void addTodo(RequestCardTodo todo) {
        todos.add(todo);
        todo.setRequestCard(this);
    }
}
