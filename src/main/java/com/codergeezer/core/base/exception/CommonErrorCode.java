package com.codergeezer.core.base.exception;

import org.springframework.http.HttpStatus;

/**
 * @author haidv
 * @version 1.0
 */
public enum CommonErrorCode implements BaseErrorCode {

    BAD_REQUEST(1,
                "bad.request",
                "Server cannot or will not process the request due to something that is to be a client error.",
                HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(2,
                 "auth.error",
                 "There was an error during authentication, requires login again.",
                 HttpStatus.UNAUTHORIZED),
    FORBIDDEN(3,
              "access.denied",
              "You do not have permission to perform this operation.",
              HttpStatus.FORBIDDEN),
    NOT_FOUND(4, "not.found.url", "No handler found for %s %s", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(5, "method.not.allowed",
                       "Request method is known by the server but is not supported by the target resource",
                       HttpStatus.METHOD_NOT_ALLOWED),
    DATA_INTEGRITY_VIOLATION(6,
                             "data.integrity.violation",
                             "An attempt to insert or update data results in violation of an integrity constraint.",
                             HttpStatus.CONFLICT),
    MISSING_REQUEST_PARAMETER(7,
                              "missing.request.parameter",
                              "Required %s parameter '%s' is not present",
                              HttpStatus.BAD_REQUEST),
    UNSUPPORTED_MEDIA_TYPE(8, "unsupported.media.type",
                           "The server refuses to accept the request because the payload format is in an unsupported format",
                           HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    MEDIA_TYPE_NOT_ACCEPTABLE(9, "media.type.not.acceptable",
                              "The request handler cannot generate a response that is acceptable by the client",
                              HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    ARGUMENT_TYPE_MISMATCH(10, "argument.type.mismatch",
                           "Required type %s parameter '%s' is not match",
                           HttpStatus.BAD_REQUEST),
    ARGUMENT_NOT_VALID(11, "argument.not.valid", "Validation failed", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND(12, "entity.not.found", "Object no longer exists in the database",
                     HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(13,
                          "server.error",
                          "There was an error processing the request, please contact the administrator!",
                          HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_TOKEN_INVALID(14,
                         "access.token.invalid",
                         "Access token invalid, create a new access token or login again!",
                         HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED(15,
                         "access.token.expired",
                         "Access token expired, create a new access token or login again!",
                         HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(16,
                          "refresh.token.invalid",
                          "The refresh token invalid, requires login again!",
                          HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(17,
                          "refresh.token.expired",
                          "The refresh token expired, requires login again!",
                          HttpStatus.UNAUTHORIZED);

    private final int code;

    private final String messageCode;

    private final String defaultMessage;

    private final HttpStatus httpStatus;

    CommonErrorCode(int code, String messageCode, String defaultMessage, HttpStatus httpStatus) {
        this.defaultMessage = defaultMessage;
        this.httpStatus = httpStatus;
        this.code = code;
        this.messageCode = messageCode;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessageCode() {
        return messageCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
