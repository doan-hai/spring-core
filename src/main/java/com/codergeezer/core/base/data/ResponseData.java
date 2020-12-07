package com.codergeezer.core.base.data;

import com.codergeezer.core.base.constant.DateConstant;
import com.codergeezer.core.base.utils.RequestUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author haidv
 * @version 1.0
 */
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String timestamp;

    private String message;

    private String service;

    private String requestId;

    private T data;

    ResponseData() {
        this.code = 0;
        this.timestamp = LocalDateTime.now()
                                      .format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS));
        this.message = "Successful!";
        this.service = RequestUtils.extractServiceName();
        this.requestId = RequestUtils.extractRequestId();
    }

    ResponseData<T> success(T data) {
        this.data = data;
        return this;
    }

    ResponseData<T> error(int code, String message) {
        this.code = code;
        this.message = message;
        return this;
    }

    ResponseData<T> error(int code, String message, T data) {
        this.data = data;
        this.code = code;
        this.message = message;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @SuppressWarnings("unused")
    public String getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public void setMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unused")
    public String getService() {
        return service;
    }

    @SuppressWarnings("unused")
    public void setService(String service) {
        this.service = service;
    }

    @SuppressWarnings("unused")
    public String getRequestId() {
        return requestId;
    }

    @SuppressWarnings("unused")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @SuppressWarnings("unused")
    public T getData() {
        return data;
    }

    @SuppressWarnings("unused")
    public void setData(T data) {
        this.data = data;
    }
}
