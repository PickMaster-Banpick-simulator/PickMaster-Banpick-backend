package com.lol.fearlessdraft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/app/banpick/**").permitAll()   // 인증 없이 허용
                .simpDestMatchers("/topic/banpick/**").permitAll() // 브로드캐스트도 모두 허용
                .anyMessage().authenticated(); // 그 외는 인증 필요
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


}
