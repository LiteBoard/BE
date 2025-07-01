package we.LiteBoard.global.util.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTokenCache implements TokenCache {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    @Override
    public void save(String key, String value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            log.error("Failed to save refresh token: key={}, ttl={}", key, ttl, e);
            throw new CustomException(ErrorCode.REDIS_STORE_FAILED);
        }
    }

    @Override
    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(prefixed(key));
        } catch (Exception e) {
            log.error("Failed to get refresh token: key={}", key, e);
            throw new CustomException(ErrorCode.REDIS_READ_FAILED);
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(prefixed(key));
        } catch (Exception e) {
            log.error("Failed to delete refresh token: key={}", key, e);
            throw new CustomException(ErrorCode.REDIS_DELETE_FAILED);
        }
    }

    private String prefixed(String key) {
        return REFRESH_TOKEN_PREFIX + key;
    }
}
