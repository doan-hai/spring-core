package com.codergeezer.core.base.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author haidv
 * @version 1.0
 */
public class ResponseUtils {

    private ResponseUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> ResponseEntity<ResponseData<T>> success() {
        return ResponseEntity.ok(new ResponseData<>(false));
    }

    public static <T> ResponseEntity<ResponseData<T>> success(T o) {
        return ResponseEntity.ok(new ResponseData<T>(false).success(o));
    }

    public static <T> ResponseEntity<ResponseData<T>> success(T o, boolean isEncrypt) {
        return ResponseEntity.ok(new ResponseData<T>(isEncrypt).success(o));
    }

    public static <T> ResponseEntity<ResponseData<T>> created() {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<>(false));
    }

    public static <T> ResponseEntity<ResponseData<T>> created(T o) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<T>(false).success(o));
    }

    public static <T> ResponseEntity<ResponseData<T>> created(T o, boolean isEncrypt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<T>(isEncrypt).success(o));
    }

    public static <T> ResponseEntity<ResponseData<T>> error(int code, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(getResponseDataError(code, message, null, false));
    }

    public static <T> ResponseEntity<ResponseData<T>> error(int code, String message, T data, HttpStatus status,
                                                            boolean isEncrypt) {
        return ResponseEntity.status(status).body(getResponseDataError(code, message, data, isEncrypt));
    }

    public static <T> ResponseData<T> getResponseDataError(int code, String message, T data, boolean isEncrypt) {
        return new ResponseData<T>(isEncrypt).error(code, message, data);
    }
}
