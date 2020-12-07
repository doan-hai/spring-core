package com.codergeezer.core.base.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "core.cache.type", havingValue = "caffeine")
@Import(CacheAutoConfiguration.class)
public class MemoryCacheAutoConfiguration {

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean("memory-cache-manager")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "core.cache.type", havingValue = "caffeine")
    public CacheManager cacheManager(final Ticker ticker, final CacheProperties cacheProperties) {
        final List<CaffeineCache> caches = new ArrayList<>();
        cacheProperties.getProperties().forEach((k, v) -> {
            CaffeineCache cache = this.buildCache(v, ticker);
            caches.add(cache);
        });
        final SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(caches);
        return manager;
    }

    private CaffeineCache buildCache(final CacheBuilder cacheBuilder, final Ticker ticker) {
        return new CaffeineCache(cacheBuilder.getCacheName(),
                                 Caffeine.newBuilder().expireAfterWrite(cacheBuilder.getExpiredTime())
                                         .maximumSize(cacheBuilder.getMaximumSize()).ticker(ticker)
                                         .build());
    }
}
