package com.github.bibenga.palabras.api;

import java.io.IOException;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WSHandler extends TextWebSocketHandler {

    // curl -v -i -N -H "Connection: Upgrade" -H "Upgrade: Websocket" -H "Sec-WebSocket-Version: 13" -H "Sec-WebSocket-Key: q4xkcO32u266gldTuKaSOw==" -H "Host: 127.0.0.1" -H "Origin: http://127.0.0.1:8080" http://127.0.0.1:8080/ws

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("ws: id={}, messages={}", session.getId(), message);
        try {
            session.sendMessage(new TextMessage("Pong"));
        } catch (IOException e) {
            log.error("kaka", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("ws: connected, id={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("ws: closed, id={}, status={}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error(String.format("ws: error, id=%s", session.getId()), exception);
    }

}
