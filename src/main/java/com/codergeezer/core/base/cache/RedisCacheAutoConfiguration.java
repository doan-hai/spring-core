package com.codergeezer.core.base.cache;

import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "core.cache.type", havingValue = "REDIS")
@Import(CacheAutoConfiguration.class)
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {

    /**
     * Add custom cache exception handling. Ignore exception when cache read / write exception
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new IgnoreExceptionCacheErrorHandler();
    }

    @Bean("redis-cache-manager")
    @ConditionalOnProperty(name = "core.cache.type", havingValue = "REDIS")
    @ConditionalOnMissingBean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory, CacheProperties cacheProperties) {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        var properties = cacheProperties.getProperties();
        properties.forEach((k, v) -> configurationMap.put(v.getCacheName(),
                                                          buildRedisCacheConfiguration(v.getExpiredTime())));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

    private RedisCacheConfiguration buildRedisCacheConfiguration(Duration duration) {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(duration)
                                      .serializeKeysWith(keyPair())
                                      .serializeValuesWith(valuePair());
    }

    private SerializationPair<String> keyPair() {
        return SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    private SerializationPair<Object> valuePair() {
        return SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
    }
}
