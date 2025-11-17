package com.pingyu.codematebackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // 【【【 1. 导入“新工具” 】】】
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 模板配置 (解决“乱码”和“LocalDateTime”陷阱)
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 1. Key 的序列化器：使用 String (明文)
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 2. Value 的序列化器：使用 JSON (明文)

        // 【【【 2. 创建“打包工” (ObjectMapper) 】】】
        ObjectMapper objectMapper = new ObjectMapper();
        // 【【【 3. “武装”打包工：注册“新工具”(JavaTimeModule) 】】】
        // (这就是那行“魔法”代码)
        objectMapper.registerModule(new JavaTimeModule());

        // (我们不再使用默认的 ObjectMapper，而是使用我们“武装”过的这一个)
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}