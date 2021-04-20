package com.example.demo.configurations

import io.lettuce.core.ClientOptions
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories


@Configuration
@EnableConfigurationProperties(RedisProperties::class)
@EnableRedisRepositories
class InfraModuleConfiguration {

    @Autowired
    private lateinit var redisProperties: RedisProperties

    @Bean(destroyMethod = "shutdown")
    fun clientResources(): ClientResources {
        return DefaultClientResources.create()
    }

    @Bean
    fun clientOptions(): ClientOptions {
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build()
    }

    @Bean
    fun lettucePoolConfig(options: ClientOptions, dcr: ClientResources): LettucePoolingClientConfiguration {
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(GenericObjectPoolConfig<Any>())
                .clientOptions(options)
                .clientResources(dcr)
                .build()
    }

    @Bean
    fun connectionFactory(lettucePoolConfig: LettucePoolingClientConfiguration): RedisConnectionFactory {

        //TODO VERIFICAR CONEXAO
        //TODO ADD SENHA REDIS
        val standaloneConfiguration = RedisStandaloneConfiguration()
        standaloneConfiguration.password = RedisPassword.of("")
        val lettuceConnectionFactory = LettuceConnectionFactory(standaloneConfiguration, lettucePoolConfig)
        lettuceConnectionFactory.validateConnection = true

        return lettuceConnectionFactory

//        return if (redisProperties.sentinel.nodes.size < 3) {
//            val standaloneConfiguration = RedisStandaloneConfiguration("localhost", 6378)
//            standaloneConfiguration.password = RedisPassword.of("RURAl03")
//            LettuceConnectionFactory(standaloneConfiguration, lettucePoolConfig)
//        } else {
//
//            val sentinelConfig = RedisSentinelConfiguration()
//                    .master(redisProperties.sentinel.master)
//            redisProperties.sentinel.nodes.forEach { s -> sentinelConfig.sentinel(s, Integer.valueOf(redisProperties.port)) }
//            sentinelConfig.password = RedisPassword.of(redisProperties.password)
//            LettuceConnectionFactory(sentinelConfig, lettucePoolConfig)
//        }
    }

    @Bean
    @ConditionalOnMissingBean(name = ["redisTemplate"])
    @Primary
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<*, *> {
        val template = RedisTemplate<String, String>()
        template.setConnectionFactory(redisConnectionFactory)
        return template
    }
//
//    @Bean
//    fun redisMessageListenerContainer(redisConnectionFactory: RedisConnectionFactory?): RedisMessageListenerContainer? {
//        val bean = RedisMessageListenerContainer()
//        bean.setConnectionFactory(redisConnectionFactory!!)
//        return bean
//    }
}