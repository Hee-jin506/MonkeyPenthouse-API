package com.monkeypenthouse.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${redis.hostname}")
    private String HOSTNAME;

    @Value("${redis.port}")
    private int PORT;

    @Value("${redis.database}")
    private int DATABASE;

    @Value("${redis.password}")
    private String PASSWORD;

    @Value("${redis.timeout}")
    private long TIMEOUT;

    // 레디스 접속을 위한 빈 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(HOSTNAME);
        config.setPort(PORT);
        config.setDatabase(DATABASE);
        config.setPassword(PASSWORD);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(TIMEOUT))
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

//    // 텍스트를 저장하기 위한 빈 생성
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(
//            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
//    ) {
//
//        StringRedisTemplate template = new StringRedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        return template;
//    }
//
//    // 객체 형태의 데이터를 저장하기 위한 빈 생성
//    @Bean
//    public RedisTemplate<String, byte[]> messagePackRedisTemplate(
//            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
//    ) {
//
//        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setEnableDefaultSerializer(false);
//
//        return template;
//    }
//
//    // 객체의 저장이나 조회 시 serialize를 위한 ObjectMapper 빈 생성
//    @Bean
//    public ObjectMapper messagePackObjectMapper() {
//        return new ObjectMapper(new MessagePackFactory())
//                .registerModule(new JavaTimeModule())
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//    }
}
