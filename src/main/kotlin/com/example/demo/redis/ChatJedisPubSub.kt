package com.example.demo.redis

import org.slf4j.LoggerFactory
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub

class ChatJedisPubSub : JedisPubSub() {
    var poolConfig: JedisPoolConfig? = null;
    var jedisPool: JedisPool? = null;
    var subscriberJedis: Jedis? = null;

    override fun onMessage(channel: String?, message: String?) {
        poolConfig = JedisPoolConfig()
        jedisPool = JedisPool(poolConfig, "localhost", 6379, 0, "")
        subscriberJedis = jedisPool!!.resource
        logger.info("Message received. Channel: {}, Msg: {}", channel, message)
        subscriberJedis?.publish(channel, message)
        System.out.println("PUBLISH REDIS")
    }

    override fun onPMessage(pattern: String?, channel: String?, message: String?) {}
    override fun onSubscribe(channel: String?, subscribedChannels: Int) {
        poolConfig = JedisPoolConfig()
        jedisPool = JedisPool(poolConfig, "localhost", 6379, 0, "")
        subscriberJedis = jedisPool!!.resource
        subscriberJedis?.subscribe(this, channel)
    }
    override fun onUnsubscribe(channel: String?, subscribedChannels: Int) {}
    override fun onPUnsubscribe(pattern: String?, subscribedChannels: Int) {}
    override fun onPSubscribe(pattern: String?, subscribedChannels: Int) {}

    companion object {
        private val logger = LoggerFactory.getLogger(JedisPubSub::class.java)
    }

    fun getPpool(): JedisPool {
        return jedisPool!!
    }
}