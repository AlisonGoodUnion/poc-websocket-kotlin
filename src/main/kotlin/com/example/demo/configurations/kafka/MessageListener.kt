package com.example.demo.configurations.kafka

import com.example.demo.dto.ChatMessagePojo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MessageListener {

    @Autowired
    var template: SimpMessagingTemplate? = null

    @KafkaListener( id = "containerMensagem", topics = ["mensagem"], containerFactory = "messageKafkaListenerContainerFactory", autoStartup = "false")
    fun greetingListener(message: ChatMessagePojo) {
        template?.convertAndSend("/topic/public/${message.idProposta}", message)
    }
}