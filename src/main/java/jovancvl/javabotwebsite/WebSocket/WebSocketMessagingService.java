package jovancvl.javabotwebsite.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketMessagingService {

    public static SimpMessagingTemplate template;

    @Autowired
    public WebSocketMessagingService(SimpMessagingTemplate template) {
        WebSocketMessagingService.template = template;
    }
}
