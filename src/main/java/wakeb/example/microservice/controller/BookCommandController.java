package wakeb.example.microservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.service.interfaces.BookCommandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling book command operations such as create, update, and delete.
 */
@RestController
@RequestMapping("/api/books")
public class BookCommandController {

    private final BookCommandService bookCommandService;

    /**
     * Constructor for injecting the BookCommandService.
     *
     * @param bookCommandService the service handling book command logic.
     */
    @Autowired
    public BookCommandController(BookCommandService bookCommandService) {
        this.bookCommandService = bookCommandService;
    }

    /**
     * Creates a new book.
     *
     * @param bookDTO the data transfer object containing book details.
     * @return the ResponseEntity containing the created book details and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookCommandService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }


    /**
     * Updates an existing book.
     *
     * @param id the unique identifier of the book to update.
     * @param bookDTO the data transfer object containing updated book details.
     * @return the ResponseEntity containing the updated book details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookCommandService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Deletes an existing book.
     *
     * @param id the unique identifier of the book to delete.
     * @return the ResponseEntity with HTTP status NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookCommandService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}