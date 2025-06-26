package we.LiteBoard.global.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthService authService;

    /**
     * 쿠키에 담긴 Access/Refresh 토큰을 모두 꺼내 응답 헤더로 내려줍니다.
     */
    @GetMapping("/token")
    public ResponseEntity<Void> issueTokensInHeader(
            @CookieValue(name = "Authorization",   required = false) String rawAccess,
            @CookieValue(name = "Refresh-Token",   required = false) String refreshToken
    ) {
        HttpHeaders headers = new HttpHeaders();

        // Access Token
        if (rawAccess != null && !jwtUtil.isExpired(rawAccess)) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + rawAccess);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        // Refresh Token 처리
        if (refreshToken != null && !jwtUtil.isExpired(refreshToken)) {
            headers.add("Refresh-Token", refreshToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    /**
     * Refresh Token으로 Access Token을 재발급 합니다.
     */
    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissue(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().build();
        }

        String newAccessToken = authService.reissueAccessToken(refreshToken);

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", newAccessToken);

        return ResponseEntity.ok(result);
    }
}
