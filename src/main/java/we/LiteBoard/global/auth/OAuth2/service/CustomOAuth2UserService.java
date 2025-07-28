package we.LiteBoard.global.auth.OAuth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.enumerate.MemberRole;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.global.auth.OAuth2.CustomOAuth2User;
import we.LiteBoard.global.auth.OAuth2.dto.GoogleResponse;
import we.LiteBoard.global.auth.OAuth2.dto.OAuth2Response;
import we.LiteBoard.global.auth.OAuth2.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String picture = oAuth2Response.getPicture();

        Member existData = memberRepository.findByUsername(username);

        Member member;
        if (existData == null) {
            member = Member.builder()
                    .username(username)
                    .email(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getName())
                    .role(MemberRole.USER)
                    .picture(picture)
                    .build();
            memberRepository.save(member);
        } else {
            member = existData.update(oAuth2Response.getEmail(), oAuth2Response.getName(), oAuth2Response.getPicture());
            memberRepository.save(member);
        }

        UserDTO userDTO = UserDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole().name())
                .picture(member.getPicture())
                .build();

        return new CustomOAuth2User(userDTO);
    }
}
