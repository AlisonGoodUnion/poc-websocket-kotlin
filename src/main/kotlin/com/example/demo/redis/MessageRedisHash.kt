package com.example.demo.redis

import com.example.demo.dto.ChatMessagePojo
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.types.Expiration

@RedisHash("Message")
data class MessageRedisHash(@Id val idProposta: Long, val messages: ArrayList<ChatMessagePojo>) {
    @TimeToLive
    private var expiration: Long = Expiration.seconds(60 * 60).expirationTime
}