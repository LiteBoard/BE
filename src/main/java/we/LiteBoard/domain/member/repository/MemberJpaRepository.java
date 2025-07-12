package we.LiteBoard.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.member.entity.Member;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByEmail(String email);
}
