package wakeb.example.microservice.exception.custom;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an attempt is made to create a book that already exists.
 */
public class BookAlreadyExistsException extends AbstractCustomException {

    /**
     * Constructs a new BookAlreadyExistsException with a specific error message.
     *
     * @param message the detail message explaining the exception.
     */
    public BookAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}