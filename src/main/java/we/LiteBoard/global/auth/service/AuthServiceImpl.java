package we.LiteBoard.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;
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
            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        String email = jwtUtil.getEmail(refreshToken);

        // Redis에 저장된 Refresh Token과 비교
        String storedRefreshToken = redisTokenCache.get(email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createAccessToken(email, role);
    }
}
