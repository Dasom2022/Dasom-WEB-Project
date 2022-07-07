package com.dama.config;

/*
import com.dain.chat.TopicSubscriptionInterceptor;
*/

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

/*
    private final TopicSubscriptionInterceptor topicSubscriptionInterceptor;
*/

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat").setAllowedOrigins("http://43.200.61.12:3333","http://localhost:3000").withSockJS();
    }

    /*@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(topicSubscriptionInterceptor);
    }*/
}
