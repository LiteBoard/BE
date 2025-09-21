package we.LiteBoard.global.auth.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class InviteJwtProvider {

    private final SecretKey key;
    private final long expirationMs = 1000L * 60 * 60 * 24 * 7; // 7일

    public InviteJwtProvider(@Value("${invite.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 초대 토큰 생성
     */
    public String createInviteToken(String email, Long projectId, ProjectRole role) {
        return Jwts.builder()
                .subject(email)
                .claim("projectId", projectId)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰을 파싱하여 반환
     */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
