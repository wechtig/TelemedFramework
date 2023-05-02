package at.communicationrtc.communicationrtc.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> userSessions = new HashMap<>();
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        String payload = message.getPayload();
        JsonObject jsonObject = JsonParser.parseString(payload).getAsJsonObject();

        String remoteUsername = jsonObject.get("name").getAsString();
        String to = remoteUsername.split(":")[1];
        String from = remoteUsername.split(":")[0];

        WebSocketSession targetSession = userSessions.get(to);
        WebSocketSession ownSession = userSessions.get(from);

        if (targetSession != null && targetSession.isOpen()) {
            targetSession.sendMessage(message);
    //        ownSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getPrincipal().getName();
        userSessions.put(username, session);
    }
}
