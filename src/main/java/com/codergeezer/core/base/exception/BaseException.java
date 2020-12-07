package com.codergeezer.core.base.exception;

import com.codergeezer.core.base.data.BaseObject;
import org.springframework.http.HttpStatus;

/**
 * Base class for all business exceptions thrown by the application.
 *
 * @author haidv
 * @version 1.0
 */
public class BaseException extends RuntimeException {

    private final BaseObject[] messageArg;

    private final Log log;

    private final int code;

    private final String messageCode;

    private final String defaultMessage;

    private final HttpStatus httpStatus;

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param message the detail message.
     */
    public BaseException(String message) {
        this(message, CommonErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     */
    public BaseException(BaseErrorCode baseErrorCode) {
        this(baseErrorCode.getMessageCode(), baseErrorCode);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param message       the detail message.
     */
    public BaseException(String message, BaseErrorCode baseErrorCode) {
        this(message, baseErrorCode, Log.SIMPLE_LOG);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param log           log
     */
    public BaseException(BaseErrorCode baseErrorCode, Log log) {
        this(baseErrorCode.getMessageCode(), baseErrorCode, log);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param log           log
     * @param message       the detail message.
     */
    public BaseException(String message, BaseErrorCode baseErrorCode, Log log) {
        this(message, baseErrorCode, null, log);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param messageArg    an array of arguments
     */
    public BaseException(BaseErrorCode baseErrorCode, Object[] messageArg) {
        this(baseErrorCode.getMessageCode(), baseErrorCode, messageArg, Log.SIMPLE_LOG);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param messageArg    an array of arguments
     * @param message       the detail message.
     */
    public BaseException(String message, BaseErrorCode baseErrorCode, Object[] messageArg) {
        this(message, baseErrorCode, messageArg, Log.SIMPLE_LOG);
    }

    /**
     * Constructs a new exception object with the specified error code.
     *
     * @param baseErrorCode {@link BaseErrorCode}
     * @param messageArg    an array of arguments
     * @param log           log
     * @param message       the detail message.
     */
    public BaseException(String message, BaseErrorCode baseErrorCode, Object[] messageArg, Log log) {
        super(message);
        this.messageArg = (BaseObject[]) messageArg;
        this.log = log;
        this.code = baseErrorCode.getCode();
        this.httpStatus = baseErrorCode.getHttpStatus();
        this.messageCode = baseErrorCode.getMessageCode();
        if (baseErrorCode instanceof CommonErrorCode) {
            this.defaultMessage = ((CommonErrorCode) baseErrorCode).getDefaultMessage();
        } else {
            this.defaultMessage = null;
        }
    }

    public Object[] getMessageArg() {
        return messageArg;
    }

    public Log getLog() {
        return log;
    }

    public int getCode() {
        return code;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public enum Log {
        FULL_LOG,
        SIMPLE_LOG
    }
}
