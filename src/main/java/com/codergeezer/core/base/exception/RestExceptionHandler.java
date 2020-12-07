package com.codergeezer.core.base.exception;

import com.codergeezer.core.base.data.ResponseData;
import com.codergeezer.core.base.data.ResponseUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.codergeezer.core.base.exception.CommonErrorCode.*;

/**
 * REST API common error handler class. <br> From the exception class thrown without being caught by the controller, the
 * error code / HTTP status code to be returned to the client is judged, converted into a common HTTP error response
 * format, and sent to the client. <br>
 *
 * @author haidv
 * @version 1.0
 */
@RestController
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Customize the response for MissingServletRequestParameterException. Triggered when a 'required' request parameter
     * is missing.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                       HttpHeaders headers, HttpStatus status,
                                                                       WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(MISSING_REQUEST_PARAMETER);
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException.
     * <p>This method logs a warning, sets the "Allow" header, and delegates to
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(METHOD_NOT_ALLOWED);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(ARGUMENT_NOT_VALID, null, this.getSubErrors(ex));
    }

    /**
     * Customize the response for HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(BAD_REQUEST);
    }

    /**
     * Customize the response for HttpMessageNotWritableException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(BAD_REQUEST);
    }

    /**
     * Customize the response for NoHandlerFoundException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers, HttpStatus status,
                                                                   WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(NOT_FOUND, new Object[]{ex.getHttpMethod(), ex.getRequestURL()});
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status,
                                                                      WebRequest request) {
        LOGGER.error(ex.getMessage());
        return this.handleError(MEDIA_TYPE_NOT_ACCEPTABLE);
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        LOGGER.error(ex.getMessage());
        return this.handleError(ENTITY_NOT_FOUND);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return this.handleError(DATA_INTEGRITY_VIOLATION);
    }

    /**
     * Handle AccessDeniedException.
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        LOGGER.error(ex.getMessage());
        return this.handleError(FORBIDDEN);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        LOGGER.error(ex.getMessage());
        return this.handleError(ARGUMENT_TYPE_MISMATCH, new Object[]{ex.getRequiredType(), ex.getName()});
    }

    /**
     * Handle BaseException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ResponseData<Object>> handleBaseException(BaseException ex) {
        List<SubError> subErrors = null;
        LOGGER.error(ex.getLocalizedMessage(), ex);
        int code = ex.getCode();
        HttpStatus httpStatus = ex.getHttpStatus();
        return ResponseUtils.error(code, ex.getMessage(), subErrors, httpStatus, false);
    }

    /**
     * Handles HttpClientErrorException
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return this.handleError(EXECUTE_THIRTY_SERVICE_ERROR);
    }

    /**
     * Handles Exception
     *
     * @param ex the exception
     * @return a {@code ResponseEntity} instance
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return this.handleError(INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings("rawtypes")
    private ResponseEntity handleError(CommonErrorCode commonError) {
        return this.handleError(commonError, null, null);
    }

    @SuppressWarnings("rawtypes")
    private ResponseEntity handleError(CommonErrorCode commonError, Object[] messageArg) {
        return this.handleError(commonError, messageArg, null);
    }

    @SuppressWarnings("rawtypes")
    private ResponseEntity handleError(CommonErrorCode commonError, Object[] messageArg, List<SubError> subErrors) {
        String message = commonError.getMessage();
        int code = commonError.getCode();
        if (messageArg != null) {
            message = String.format(message, messageArg);
        }
        HttpStatus httpStatus = commonError.getHttpStatus();
        return ResponseUtils.error(code, message, subErrors, httpStatus, false);
    }

    private List<SubError> getSubErrors(MethodArgumentNotValidException e) {
        List<SubError> subErrors = new ArrayList<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            SubError subError = new SubError(fieldError.getField(),
                                             fieldError.getRejectedValue(),
                                             fieldError.getDefaultMessage());
            subErrors.add(subError);
        }
        return subErrors;
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Getter
    @RequiredArgsConstructor
    public static class SubError {

        private final String fieldName;

        private final Object fieldValue;

        private final String message;
    }
}