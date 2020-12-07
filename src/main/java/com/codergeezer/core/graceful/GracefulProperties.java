package com.codergeezer.core.graceful;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author haidv
 * @version 1.0
 */
@ConfigurationProperties(prefix = "core.graceful.shutdown")
public class GracefulProperties {

    private Duration timeout;

    private Duration wait;

    public Duration getTimeout() {
        return this.timeout;
    }

    public void setTimeout(final Duration timeout) {
        this.timeout = timeout;
    }

    public Duration getWait() {
        return this.wait;
    }

    public void setWait(final Duration wait) {
        this.wait = wait;
    }
}
