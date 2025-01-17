package wakeb.example.microservice.service.interfaces;

import wakeb.example.microservice.dto.book.BookDTO;

/**
 * Service interface defining command operations for books such as create, update, and delete.
 */
public interface BookCommandService {

    /**
     * Creates a new book.
     *
     * @param bookDTO the data transfer object containing book details.
     * @return the created book as a data transfer object.
     */
    BookDTO createBook(BookDTO bookDTO);

    /**
     * Updates an existing book.
     *
     * @param id      the unique identifier of the book to update.
     * @param bookDTO the data transfer object containing updated book details.
     * @return the updated book as a data transfer object.
     */
    BookDTO updateBook(Long id, BookDTO bookDTO);

    /**
     * Deletes a book by its unique identifier.
     *
     * @param id the unique identifier of the book to delete.
     */
    void deleteBook(Long id);
}