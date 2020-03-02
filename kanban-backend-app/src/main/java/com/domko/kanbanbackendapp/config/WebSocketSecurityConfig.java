package com.domko.kanbanbackendapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//       messages.simpDestMatchers("/websocket").permitAll()
//               .anyMessage().permitAll();
        messages.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.SUBSCRIBE, SimpMessageType.HEARTBEAT,
                SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        // We need to access this directly from apps, so can't do cross-site checks
        return true;
    }
}
