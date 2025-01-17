package wakeb.example.microservice.exception;

import lombok.Getter;

/**
 * Enumeration of common exception messages used throughout the application.
 */
@Getter
public enum ExceptionMessagesEnum {

    BOOK_NOT_FOUND("Book not found"),
    BOOK_ALREADY_EXISTS("Book already exists");

    private final String message;

    /**
     * Constructs an enum instance with the specified message.
     *
     * @param message the message associated with the exception.
     */
    ExceptionMessagesEnum(String message) {
        this.message = message;
    }

}

