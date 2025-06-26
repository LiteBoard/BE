package we.LiteBoard.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.util.redis.RedisTokenCache;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;
    private final RedisTokenCache redisTokenCache;

    @Override
    public String reissueAccessToken(String refreshToken) {
        if (jwtUtil.isExpired(refreshToken)) {
            // 예외 처리
        }

        String username = jwtUtil.getUsername(refreshToken);

        // Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = redisTokenCache.get(username);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            // 예외 처리
        }

        String role = jwtUtil.getRole(refreshToken);

        return jwtUtil.createAccessToken(username, role);
    }
}
