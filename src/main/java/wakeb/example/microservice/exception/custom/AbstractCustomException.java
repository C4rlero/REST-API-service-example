package wakeb.example.microservice.exception.custom;

import org.springframework.http.HttpStatus;

/**
 * An abstract custom exception class that encapsulates an HTTP status code along with the error message.
 * All custom exceptions should extend from this class.
 */
public abstract class AbstractCustomException extends RuntimeException {
    private final HttpStatus status;

    /**
     * Constructs a new custom exception with the specified HTTP status and detail message.
     *
     * @param status  the HTTP status code representing the error.
     * @param message the detail message.
     */
    public AbstractCustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Returns the HTTP status associated with the exception.
     *
     * @return the HTTP status.
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Returns a machine-readable error code. By default, it returns the class name in uppercase.
     *
     * @return the error code.
     */
    public String getErrorCode() {
        return this.getClass().getSimpleName().toUpperCase();
    }
}