package com.longer.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // 用来配置 redisTemplate 的序列化方式
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        // key 采用 String 的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // value 序列化方式采用 jackson
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash 的 value 序列化方式采用 jackson
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
