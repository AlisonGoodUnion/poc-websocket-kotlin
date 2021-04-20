package com.example.demo.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*")
        registry.addEndpoint("/ws").setAllowedOrigins().withSockJS()


    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic")
    }


//    override configureClientInboundChannel(registration: ChannelRegistration ) {
//        registration.interceptors(new ChannelInterceptor() {
//            override public Message<?> preSend(Message<?> message, MessageChannel channel) {
//            StompHeaderAccessor accessor =
//            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                Authentication user = ... ; // access authentication header(s)
//                accessor.setUser(user);
//            }
//            return message;
//        }
//        })
//    }
}