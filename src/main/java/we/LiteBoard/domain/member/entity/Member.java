package we.LiteBoard.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
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
    private String role;

    @OneToMany(mappedBy = "member")
    private List<MemberProject> memberProjects = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<RequestCard> receivedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<RequestCard> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Notification> sentNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Notification> receivedNotifications = new ArrayList<>();


    public Member update(String email, String name) {
        this.email = email;
        this.name = name;
        return this;
    }
}
