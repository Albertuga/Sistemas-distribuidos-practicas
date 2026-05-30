package com.sistemasdistr.basico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Esta es la URL a la que se conectará nuestro JavaScript
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Canal al que se suscriben los usuarios para recibir mensajes
        registry.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que envían los usuarios al servidor
        registry.setApplicationDestinationPrefixes("/app");
    }

}
