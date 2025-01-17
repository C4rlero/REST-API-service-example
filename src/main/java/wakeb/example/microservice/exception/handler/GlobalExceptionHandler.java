package wakeb.example.microservice.exception.handler;

import wakeb.example.microservice.dto.error.ProblemDetail;
import wakeb.example.microservice.dto.error.ProblemDetailMapper;
import wakeb.example.microservice.exception.custom.AbstractCustomException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler that intercepts and handles exceptions thrown by the controllers.
 * It converts exceptions into RFC 7807 compliant error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles custom exceptions extending from {@link AbstractCustomException}.
     *
     * @param ex      the custom exception.
     * @param request the current web request.
     * @return a ResponseEntity containing the {@link ProblemDetail} and corresponding HTTP status.
     */
    @ExceptionHandler(AbstractCustomException.class)
    public ResponseEntity<wakeb.example.microservice.dto.error.ProblemDetail> handleCustomException(AbstractCustomException ex, WebRequest request) {
        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/" + ex.getClass().getSimpleName(),
                ex.getClass().getSimpleName(),
                ex.getStatus(),
                ex.getMessage(),
                request,
                null,
                ex.getErrorCode()
        );
        logger.warn("Handled custom exception: {}", ex.getMessage());
        return new ResponseEntity<>(problemDetail, ex.getStatus());
    }

    /**
     * Handles validation errors triggered by @Valid annotations in DTOs.
     *
     * @param ex      the exception containing validation errors.
     * @param headers the HTTP headers.
     * @param status  the HTTP status.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status BAD_REQUEST.
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/validation-error",
                "Validation Error",
                HttpStatus.BAD_REQUEST,
                "Validation failed for the request.",
                request,
                errors,
                "VALIDATION_ERROR"
        );
        logger.warn("Validation errors: {}", errors);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles missing servlet request parameters.
     *
     * @param ex      the exception indicating a missing parameter.
     * @param headers the HTTP headers.
     * @param status  the HTTP status.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status BAD_REQUEST.
     */
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/missing-parameter",
                "Missing Parameter",
                HttpStatus.BAD_REQUEST,
                error,
                request,
                null,
                "MISSING_PARAMETER"
        );
        logger.warn("Missing parameter: {}", error);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where no handler is found for the given request.
     *
     * @param ex      the exception indicating no handler was found.
     * @param headers the HTTP headers.
     * @param status  the HTTP status.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status NOT_FOUND.
     */
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/no-handler-found",
                "No Handler Found",
                HttpStatus.NOT_FOUND,
                error,
                request,
                null,
                "NO_HANDLER_FOUND"
        );
        logger.warn("No handler found: {}", error);
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles constraint violations occurring during request processing.
     *
     * @param ex      the exception containing constraint violations.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<wakeb.example.microservice.dto.error.ProblemDetail> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage())
        );

        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/constraint-violation",
                "Constraint Violation",
                HttpStatus.BAD_REQUEST,
                "One or more validation errors occurred.",
                request,
                errors,
                "CONSTRAINT_VIOLATION"
        );
        logger.warn("Constraint violation: {}", errors);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic exceptions not handled by other specific methods.
     *
     * @param ex      the exception.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<wakeb.example.microservice.dto.error.ProblemDetail> handleAllExceptions(Exception ex, WebRequest request) {
        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/internal-server-error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                request,
                null,
                "INTERNAL_SERVER_ERROR"
        );
        logger.error("Unhandled exception: ", ex);
        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles method argument type mismatches.
     *
     * @param ex      the exception indicating a type mismatch.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<wakeb.example.microservice.dto.error.ProblemDetail> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                                                               WebRequest request) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getSimpleName();

        wakeb.example.microservice.dto.error.ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/method-argument-type-mismatch",
                "Method Argument Type Mismatch",
                HttpStatus.BAD_REQUEST,
                error,
                request,
                null,
                "ARGUMENT_TYPE_MISMATCH"
        );
        logger.warn("Method argument type mismatch: {}", error);
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when the HTTP message is not readable, such as malformed JSON.
     *
     * @param ex      the exception indicating the message is not readable.
     * @param headers the HTTP headers.
     * @param status  the HTTP status.
     * @param request the current web request.
     * @return a ResponseEntity with the {@link ProblemDetail} and HTTP status BAD_REQUEST.
     */
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ProblemDetail problemDetail = ProblemDetailMapper.toProblemDetail(
                "https://example.com/probs/message-not-readable",
                "Malformed JSON Request",
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request",
                request,
                null,
                "MESSAGE_NOT_READABLE"
        );
        logger.warn("Malformed JSON request: {}", ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}