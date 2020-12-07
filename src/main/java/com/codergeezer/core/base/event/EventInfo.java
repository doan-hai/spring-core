package com.codergeezer.core.base.event;

import java.util.UUID;

/**
 * @author haidv
 * @version 1.0
 */
public class EventInfo {

    public static final int DEFAULT_RETRY_EVENT = 3;

    private final String id;

    private final Object what;

    private final CoreEvent event;

    private final boolean isSync;

    private final int retry;

    public EventInfo(Object what, CoreEvent event) {
        this(what, event, DEFAULT_RETRY_EVENT);
    }

    public EventInfo(Object what, CoreEvent event, int retry) {
        this(what, false, event, retry);
    }

    public EventInfo(Object what, boolean isSync, CoreEvent event) {
        this(what, isSync, event, DEFAULT_RETRY_EVENT);
    }

    public EventInfo(Object what, boolean isSync, CoreEvent event, int retry) {
        this(UUID.randomUUID().toString(), what, isSync, event, retry);
    }

    public EventInfo(String id, Object what, boolean isSync, CoreEvent event, int retry) {
        this.id = id;
        this.what = what;
        this.event = event;
        this.isSync = isSync;
        this.retry = retry;
    }

    public String getId() {
        return id;
    }

    public Object getWhat() {
        return what;
    }

    public CoreEvent getEvent() {
        return event;
    }

    public boolean isSync() {
        return isSync;
    }

    public int getRetry() {
        return retry;
    }
}
