package we.LiteBoard.domain.member.service;

import we.LiteBoard.domain.member.entity.Member;

public interface MemberService {

    Member findByEmail(String email);
}
