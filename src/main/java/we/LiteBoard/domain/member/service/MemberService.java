package we.LiteBoard.domain.member.service;

import we.LiteBoard.domain.member.dto.MemberRequestDTO;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.member.entity.Member;

public interface MemberService {
    Member findByEmail(String email);
    MemberResponseDTO.MyInfo toggleNoticeStatus(Member member);
    MemberResponseDTO.MyInfo changeNickName(MemberRequestDTO.UpdateName request, Member member);
}
