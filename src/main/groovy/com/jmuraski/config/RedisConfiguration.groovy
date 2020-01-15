package com.jmuraski.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfiguration {

    @Value('${redis.host.name}')
    String redisHostName


    @Value('${redis.host.port}')
    int redisHostPort

    @Bean
    JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHostName, redisHostPort)
        return new JedisConnectionFactory(config)
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>()
        template.setConnectionFactory(redisConnectionFactory())
        return template
    }
}
