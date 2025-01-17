package wakeb.example.microservice.exception.custom;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a book is not found.
 */
public class BookNotFoundException extends AbstractCustomException {

    /**
     * Constructs a new BookNotFoundException with a specific error message.
     *
     * @param message the detail message explaining the exception.
     */
    public BookNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}