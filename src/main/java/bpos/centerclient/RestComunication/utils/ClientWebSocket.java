package bpos.centerclient.RestComunication.utils;

import org.springframework.messaging.simp.stomp.StompSession;

import java.util.concurrent.CopyOnWriteArrayList;
public class ClientWebSocket {
    private static ClientWebSocket instance;
    private StompSession session;
    private final CopyOnWriteArrayList<WebSocketMessageListener> listeners = new CopyOnWriteArrayList<>();
    private boolean connected = false;

    private ClientWebSocket() {
    }
}
