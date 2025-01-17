package wakeb.example.microservice.controller;

import org.springframework.http.ResponseEntity;
import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.service.interfaces.BookQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling book query operations such as retrieving one or all books.
 */
@RestController
@RequestMapping("/api/books")
public class BookQueryController {

    private final BookQueryService bookQueryService;


    /**
     * Constructor for injecting the BookQueryService.
     *
     * @param bookQueryService the service handling book query logic.
     */
    @Autowired
    public BookQueryController(BookQueryService bookQueryService) {
        this.bookQueryService = bookQueryService;
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id the unique identifier of the book.
     * @return the ResponseEntity containing the found book details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookQueryService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Retrieves a list of all books.
     *
     * @return the ResponseEntity containing a list of all books.
     */
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookQueryService.findAllBooks();
        return ResponseEntity.ok(books);
    }
}