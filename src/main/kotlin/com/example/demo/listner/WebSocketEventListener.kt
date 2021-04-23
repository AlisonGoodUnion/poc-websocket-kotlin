package com.example.demo.listner

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent


@Component
class WebSocketEventListener {

    @Autowired
    private val messagingTemplate: SimpMessageSendingOperations? = null

    @Autowired
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry? = null

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent?) {
        logger.info("Received a new web socket connection")
        val listenerContainer =
            kafkaListenerEndpointRegistry!!.getListenerContainer("containerMensagem")
        listenerContainer.start()
    }

//    @EventListener
//    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
//        val headerAccessor = StompHeaderAccessor.wrap(event.message)
//        val username = headerAccessor.sessionAttributes!!["username"] as String?
//        val idProposta = headerAccessor.sessionAttributes!!["idProposta"] as Long
//        if (username != null) {
//            logger.info("User Disconnected : $username")
//            val chatMessagePojo = ChatMessagePojo(
//                    username = username,
//                    idProposta = idProposta
//            )
//            messagingTemplate?.convertAndSend("/topic/public/$idProposta", chatMessagePojo)
//        }
//    }

    companion object {
        private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)
    }
}