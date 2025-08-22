package we.LiteBoard.global.auth.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {


        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public Long getId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
    }

    public String createAccessToken(Long id, String email, String role) {
        long accessTokenExpireMs = 1000L * 60 * 60; // 60분
        return createJwt(id, email, role, accessTokenExpireMs);
    }

    public String createRefreshToken(Long id, String email, String role) {
        long refreshTokenExpireMs = 1000L * 60 * 60 * 24 * 3; // 3일
        return createJwt(id, email, role, refreshTokenExpireMs);
    }

    public String createJwt(Long id, String email, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("id", id)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
