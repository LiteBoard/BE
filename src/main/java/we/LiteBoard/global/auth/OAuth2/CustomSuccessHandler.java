package we.LiteBoard.global.auth.OAuth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import we.LiteBoard.domain.member.service.MemberInviteService;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.util.redis.TokenCache;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final TokenCache tokenCache;
    private final MemberInviteService inviteService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = customUser.getUserDTO().getId();
        String email = customUser.getEmail();
        String role  = authentication.getAuthorities().iterator().next().getAuthority();

        String refreshToken = jwtUtil.createRefreshToken(userId, email, role);
        tokenCache.save("auth:refresh:" + email, refreshToken, Duration.ofDays(3));
        response.addCookie(createCookie("Refresh-Token", refreshToken));




        String inviteToken = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("inviteToken".equals(c.getName())) {
                    inviteToken = c.getValue();
                    Cookie del = new Cookie("inviteToken", "");
                    del.setPath("/");
                    del.setMaxAge(0);
                    response.addCookie(del);
                    break;
                }
            }
        }

        // InviteToken 유무에 따라 초대, 로그인 흐름 분리
        if (inviteToken != null) {
            Long memberId = ((CustomOAuth2User)authentication.getPrincipal())
                    .getUserDTO().getId();
            inviteService.acceptInvite(inviteToken, memberId);
            getRedirectStrategy().sendRedirect(
                    request, response,
                    "http://localhost:3000/invite/success"
            );
        } else {
            getRedirectStrategy().sendRedirect(
                    request, response,
                    "http://localhost:3000/auth/callback/google"
            );
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
