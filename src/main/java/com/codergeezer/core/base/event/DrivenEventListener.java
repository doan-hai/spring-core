package com.codergeezer.core.base.event;

import com.codergeezer.core.base.exception.BaseException;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.codergeezer.core.base.constant.RequestConstant.REQUEST_ID;

/**
 * @author haidv
 * @version 1.0
 */
public abstract class DrivenEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrivenEventListener.class);

    protected abstract String className();

    protected abstract TaskExecutor handleEventExecutor();

    protected abstract void processHandleErrorEventAsync(EventInfo eventInfo);

    protected abstract void processLogHandleEventAsync(EventInfo eventInfo);

    @EventListener
    public void handleEvent(EventInfo eventInfo) {
        if (!eventInfo.isSync()) {
            handleEventExecutor().execute(() -> {
                ThreadContext.put(REQUEST_ID, eventInfo.getId());
                processLogHandleEventAsync(eventInfo);
                routerEventHandle(eventInfo);
                ThreadContext.clearAll();
            });
        } else {
            routerEventHandle(eventInfo);
        }
    }

    private void routerEventHandle(EventInfo eventInfo) {
        CoreEvent event = eventInfo.getEvent();
        if (event == null) {
            LOGGER.warn("The event to be handled was not found");
            return;
        }
        LOGGER.info(String.format("Start handle event: %s id: %s", event.getEventName(), eventInfo.getId()));
        String handleEventClassName = event.getHandleEventClassName();
        String handleEventFunctionName = event.getHandleEventFunctionName();
        try {
            Method m;
            Object obj;
            Class<?> c;
            if (handleEventClassName != null) {
                c = Class.forName(handleEventClassName);
            } else {
                c = Class.forName(className());
            }
            m = c.getDeclaredMethod(handleEventFunctionName, Object.class);
            obj = c.getDeclaredConstructor().newInstance();
            invokeHandleMethod(0, eventInfo.getRetry() + 1, m, obj, eventInfo);
        } catch (NoSuchMethodException e) {
            LOGGER.warn(String.format("Method %s not found", handleEventFunctionName), e);
        } catch (ClassNotFoundException e) {
            LOGGER.warn(String.format("Class %s not found", handleEventClassName), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            LOGGER.error("init instance false", e);
        }
        LOGGER.info(String.format("End handle event: %s id: %s", event.getEventName(), eventInfo.getId()));
    }

    private void invokeHandleMethod(int index, int totalNumberExec, Method m, Object obj, EventInfo eventInfo) {
        CoreEvent event = eventInfo.getEvent();
        if (index >= totalNumberExec && !eventInfo.isSync()) {
            processHandleErrorEventAsync(eventInfo);
            return;
        }
        if (index > 0) {
            LOGGER.info(String.format("try the %sth event handling %s again", index, event.getEventName()));
        }
        try {
            m.invoke(obj, eventInfo.getWhat());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof BaseException) {
                throw (BaseException) e.getTargetException();
            }
            LOGGER.error(String.format("handle event %s error", event.getEventName()), e);
            invokeHandleMethod(index + 1, totalNumberExec, m, obj, eventInfo);
        } catch (Exception e) {
            LOGGER.error(String.format("handle event %s error", event.getEventName()), e);
            invokeHandleMethod(index + 1, totalNumberExec, m, obj, eventInfo);
        }
    }
}
