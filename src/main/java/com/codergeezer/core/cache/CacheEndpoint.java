package com.codergeezer.core.cache;

import com.codergeezer.core.base.data.ResponseData;
import com.codergeezer.core.base.data.ResponseUtils;
import com.codergeezer.core.base.exception.BaseException;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.codergeezer.core.base.exception.CommonErrorCode.BAD_REQUEST;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
@ConditionalOnProperty(name = "core.cache.type")
@ConditionalOnBean(CacheManager.class)
public class CacheEndpoint {

    private final CacheManager cacheManager;

    @Autowired
    public CacheEndpoint(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/cacheables")
    public ResponseEntity<ResponseData<Map<String, Object>>> getCacheables(
            @RequestParam(required = false) String cacheName,
            @RequestParam(required = false) Object cacheKey) {
        if (cacheManager instanceof SimpleCacheManager) {
            return ResponseUtils.success(getCaffeineCache(cacheName, cacheKey));
        }
        return ResponseUtils.success(getRedisCache(cacheName, cacheKey));
    }

    @DeleteMapping("/cacheables")
    public ResponseEntity<ResponseData<Object>> deleteCacheables(@RequestParam(required = false) String cacheName,
                                                                 @RequestParam(required = false) Object cacheKey) {
        if (!StringUtils.isEmpty(cacheName)) {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                if (cacheKey != null && !StringUtils.isEmpty(cacheKey.toString())) {
                    cache.evict(cacheKey);
                } else {
                    cache.clear();
                }
            }
        } else {
            Collection<String> cacheNames = cacheManager.getCacheNames();
            cacheNames.forEach(tmp -> {
                org.springframework.cache.Cache cache = cacheManager.getCache(tmp);
                if (cache != null) {
                    cache.clear();
                }
            });
        }
        return ResponseUtils.success();
    }

    private Map<String, Object> getCaffeineCache(String cacheName, Object cacheKey) {
        SimpleCacheManager simpleCacheManager = (SimpleCacheManager) cacheManager;
        if (!StringUtils.isEmpty(cacheName)) {
            Map<String, Object> map = new HashMap<>();
            CaffeineCache caffeineCache = (CaffeineCache) simpleCacheManager.getCache(cacheName);
            if (caffeineCache != null) {
                if (cacheKey != null && !StringUtils.isEmpty(cacheKey.toString())) {
                    return getStringObjectMap(cacheName, cacheKey, map, caffeineCache.get(cacheKey));
                } else {
                    Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                    map.put(cacheName, nativeCache.asMap());
                    return map;
                }
            }
            map.put(cacheName, null);
            return map;
        } else {
            Map<String, Object> map = new HashMap<>();
            Collection<String> cacheNames = simpleCacheManager.getCacheNames();
            cacheNames.forEach(tmp -> {
                CaffeineCache caffeineCache = (CaffeineCache) simpleCacheManager.getCache(tmp);
                if (caffeineCache != null) {
                    Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
                    map.put(tmp, nativeCache.asMap());
                } else {
                    map.put(tmp, null);
                }
            });
            return map;
        }
    }

    private Map<String, Object> getRedisCache(String cacheName, Object cacheKey) {
        if (cacheKey == null || StringUtils.isEmpty(cacheKey.toString()) || StringUtils.isEmpty(cacheName)) {
            throw new BaseException(BAD_REQUEST);
        }
        RedisCacheManager redisCacheManager = (RedisCacheManager) cacheManager;
        Map<String, Object> map = new HashMap<>();
        RedisCache redisCache = (RedisCache) redisCacheManager.getCache(cacheName);
        if (redisCache != null) {
            return getStringObjectMap(cacheName, cacheKey, map, redisCache.get(cacheKey));
        }
        map.put(cacheName, null);
        return map;
    }

    private Map<String, Object> getStringObjectMap(String cacheName, Object cacheKey, Map<String, Object> map,
                                                   org.springframework.cache.Cache.ValueWrapper valueWrapper) {
        Map<Object, Object> objectMap = new HashMap<>();
        if (valueWrapper != null) {
            objectMap.put(cacheKey, valueWrapper.get());
        } else {
            objectMap.put(cacheKey, null);
        }
        map.put(cacheName, objectMap);
        return map;
    }
}
