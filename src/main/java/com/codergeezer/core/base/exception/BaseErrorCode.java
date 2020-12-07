package com.codergeezer.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author haidv
 * @version 1.0
 */
public interface BaseErrorCode {

    String getMessageCode();

    int getCode();

    HttpStatus getHttpStatus();
}
