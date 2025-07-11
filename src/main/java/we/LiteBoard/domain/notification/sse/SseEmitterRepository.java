package we.LiteBoard.domain.notification.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public void save(Long memberId, SseEmitter emitter) {
        emitterMap.put(memberId, emitter);
    }

    public void delete(Long memberId) {
        emitterMap.remove(memberId);
    }

    public void send(Long memberId, NotificationResponseDTO.Detail data) {
        SseEmitter emitter = emitterMap.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(data));
            } catch (IOException e) {
                emitterMap.remove(memberId);
            }
        }
    }
}

