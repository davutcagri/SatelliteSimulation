package com.davutcagri.satsim.config;

import com.davutcagri.satsim.communication.LinkWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class LinkEndpointConfiguration implements WebSocketConfigurer {

    private static final String LINK_ENDPOINT = "/link";

    private final LinkWebSocketHandler linkWebSocketHandler;

    public LinkEndpointConfiguration(LinkWebSocketHandler linkWebSocketHandler) {
        this.linkWebSocketHandler = linkWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(linkWebSocketHandler, LINK_ENDPOINT);
    }
}
