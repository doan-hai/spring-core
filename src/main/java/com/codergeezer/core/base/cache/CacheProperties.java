package com.codergeezer.core.base.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@ConfigurationProperties(prefix = "core.cache")
public class CacheProperties {

    private CacheType type;

    private Map<String, CacheBuilder> properties;

    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }

    public Map<String, CacheBuilder> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, CacheBuilder> properties) {
        this.properties = properties;
    }
}
