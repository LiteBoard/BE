package we.LiteBoard.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.dto.MemberRequestDTO;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public MemberResponseDTO.MyInfo toggleNoticeStatus(Member member) {
        member.toggleNotificationEnabled();
        return MemberResponseDTO.MyInfo.from(member);
    }

    @Override
    @Transactional
    public MemberResponseDTO.MyInfo changeNickName(MemberRequestDTO.UpdateName request, Member member) {
        member.updateNickName(request.nickName());
        return MemberResponseDTO.MyInfo.from(member);
    }
}
