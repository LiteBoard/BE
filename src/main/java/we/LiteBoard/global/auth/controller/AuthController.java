package we.LiteBoard.global.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.auth.service.AuthService;
import we.LiteBoard.global.util.redis.TokenCache;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthService authService;
    private final TokenCache tokenCache;

    @GetMapping("/token")
    @Operation(summary = "토큰 발급", description = "쿠키에 담긴 Refresh Token을 꺼내 응답 헤더로 내려줍니다.")
    public ResponseEntity<Void> issueTokensInHeader(
            @CookieValue(name = "Refresh-Token", required = false) String refreshToken
    ) {
        if (refreshToken == null || jwtUtil.isExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Refresh Token이 Redis에 존재하는지 확인
        Long id = jwtUtil.getId(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        String storedRefreshToken = tokenCache.get(email);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Access Token 재발급
        String role = jwtUtil.getRole(refreshToken);
        String newAccessToken = jwtUtil.createAccessToken(id, email, role);

        // 헤더에 토큰 담아서 응답
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
        headers.set("Refresh-Token", refreshToken);

        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/reissue")
    @Operation(summary = "Access Token 재발급", description = "Refresh Token으로 Access Token을 재발급 합니다.")
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
