package we.LiteBoard.domain.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.Duration;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberInviteController {

    @GetMapping("/accept-invite")
    public void acceptInvite(
            @RequestParam("token") String token,
            HttpServletResponse response
    ) throws IOException {

        ResponseCookie cookie = ResponseCookie.from("inviteToken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofMinutes(10))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        response.sendRedirect("/oauth2/authorization/google");
    }
}
