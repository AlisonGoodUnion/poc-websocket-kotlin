package com.example.demo.configurations.kafka

import com.example.demo.dto.ChatMessagePojo
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value(value = "localhost:9092")
    private val bootstrapAddress: String? = null
    fun consumerFactory(groupId: String?): ConsumerFactory<String, String> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }

    fun messageConsumerFactory(): ConsumerFactory<String, ChatMessagePojo> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = "mensagem"
        return DefaultKafkaConsumerFactory(
            props, StringDeserializer(), JsonDeserializer(
                ChatMessagePojo::class.java
            )
        )
    }

    @Bean
    fun messageKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ChatMessagePojo> {
        val factory: ConcurrentKafkaListenerContainerFactory<String, ChatMessagePojo> =
            ConcurrentKafkaListenerContainerFactory<String, ChatMessagePojo>()
        factory.consumerFactory = messageConsumerFactory()
        return factory
    }
}