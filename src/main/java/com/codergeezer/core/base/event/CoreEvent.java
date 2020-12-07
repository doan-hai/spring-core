package com.codergeezer.core.base.event;

/**
 * @author haidv
 * @version 1.0
 */
public interface CoreEvent {

    String getEventName();

    String getHandleEventClassName();

    String getHandleEventFunctionName();
}
