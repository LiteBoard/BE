package we.LiteBoard.global.util.redis;

import java.time.Duration;

public interface TokenCache {
    void save(String key, String value, Duration ttl);
    String get(String key);
    void delete(String key);
}
