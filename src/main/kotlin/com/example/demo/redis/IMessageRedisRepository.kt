package com.example.demo.redis

import com.example.demo.redis.MessageRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IMessageRedisRepository : CrudRepository<MessageRedisHash, Long>