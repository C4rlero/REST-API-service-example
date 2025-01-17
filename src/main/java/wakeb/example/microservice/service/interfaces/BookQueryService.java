package wakeb.example.microservice.service.interfaces;

import wakeb.example.microservice.dto.book.BookDTO;

import java.util.List;

/**
 * Service interface defining query operations for retrieving books.
 */
public interface BookQueryService {

    /**
     * Finds a book by its unique identifier.
     *
     * @param id the unique identifier of the book.
     * @return the book as a data transfer object.
     */
    BookDTO findBookById(Long id);

    /**
     * Retrieves all books.
     *
     * @return a list of data transfer objects representing all books.
     */
    List<BookDTO> findAllBooks();
}