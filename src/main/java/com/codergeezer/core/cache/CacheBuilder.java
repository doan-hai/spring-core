package com.codergeezer.core.cache;

import java.time.Duration;

/**
 * @author haidv
 * @version 1.0
 */
public class CacheBuilder {

    private String cacheName;

    private Duration expiredTime;

    private int maximumSize;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Duration getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Duration expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }
}
