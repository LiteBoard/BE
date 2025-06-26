package we.LiteBoard.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisTokenCache implements TokenCache {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    @Override
    public void save(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + key);
    }
}
