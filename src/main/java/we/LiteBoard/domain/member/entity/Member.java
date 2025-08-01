package we.LiteBoard.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.enumerate.MemberRole;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.taskMember.entity.TaskMember;
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
    private String nickname;
    private String email;
    private String picture;

    @Enumerated(EnumType.STRING)
    private MemberRole role; // 과금 등급

    @Builder.Default
    @Column(nullable = false)
    private boolean notificationEnabled = true;


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberProject> memberProjects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskMember> taskMembers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestCard> sentRequests = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> sentNotifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> receivedNotifications = new ArrayList<>();


    public Member update(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        return this;
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void toggleNotificationEnabled() {
        this.notificationEnabled = !this.notificationEnabled;
    }

    /** 연관 관계 편의 메서드 */
    public void addMemberProject(MemberProject memberProject) {
        this.memberProjects.add(memberProject);
        memberProject.assignMember(this);
    }

    public void addTaskMember(TaskMember tm) {
        taskMembers.add(tm);
        tm.assignMember(this);
    }
}
