package we.LiteBoard.domain.member.repository;

import we.LiteBoard.domain.member.entity.Member;

import java.util.List;

public interface MemberRepository {
    List<Member> findAll();
    Member getById(Long id);
    Member findByUsername(String username);
    void save(Member member);
    Member findByEmail(String email);
    List<Member> findAllById(List<Long> memberIds);
}
