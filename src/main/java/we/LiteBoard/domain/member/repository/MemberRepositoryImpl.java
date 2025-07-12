package we.LiteBoard.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    @Override
    public Member getById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
    }

    @Override
    public Member findByUsername(String username) {
        return memberJpaRepository.findByUsername(username);
    }

    @Override
    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }
}
