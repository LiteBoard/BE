package we.LiteBoard.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.enumerate.MemberRole;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.todo.entity.Todo;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Task> tasks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver")
    private List<RequestCard> receivedRequests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender")
    private List<RequestCard> sentRequests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender")
    private List<Notification> sentNotifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver")
    private List<Notification> receivedNotifications = new ArrayList<>();


    public Member update(String email, String name) {
        this.email = email;
        this.name = name;
        return this;
    }
}
