package com.github.bibenga.palabras.api;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class WSHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        log.info("ws: {}", message);
    }

}
