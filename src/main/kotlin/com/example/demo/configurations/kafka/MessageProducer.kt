package com.example.demo.configurations.kafka

import com.example.demo.dto.ChatMessagePojo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer {

    @Autowired
    var kafkaTemplate: KafkaTemplate<String, ChatMessagePojo>? = null

    @Value(value = "mensagem")
    var nomeTopico: String? = null

    fun sendMessage(greeting: ChatMessagePojo) {
        kafkaTemplate!!.send(nomeTopico!!, greeting)
    }
}