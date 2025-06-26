package we.LiteBoard.domain.member.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "MEMBER")
@Getter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;
    private String name;
    private String email;
    private String role;

    public Member update(String email, String name) {
        this.email = email;
        this.name = name;
        return this;
    }
}
