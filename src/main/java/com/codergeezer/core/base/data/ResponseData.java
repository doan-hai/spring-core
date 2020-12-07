package com.codergeezer.core.base.data;

import com.codergeezer.core.base.constant.DateConstant;
import com.codergeezer.core.base.utils.RequestUtils;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author haidv
 * @version 1.0
 */
@Getter
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String timestamp;

    private final String service;

    private final String requestId;

    private boolean isEncrypt;

    private int code;

    private String message;

    private T data;

    ResponseData(boolean isEncrypt) {
        this.code = 0;
        this.timestamp = LocalDateTime.now()
                                      .format(DateTimeFormatter.ofPattern(DateConstant.STR_PLAN_DD_MM_YYYY_HH_MM_SS));
        this.message = "Successful!";
        this.service = RequestUtils.extractServiceName();
        this.requestId = RequestUtils.extractRequestId();
        this.isEncrypt = isEncrypt;
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

    public void setData(T data) {
        this.data = data;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }
}
