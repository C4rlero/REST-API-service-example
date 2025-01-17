package wakeb.example.microservice.service.implementation;

import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.dto.book.BookDTOMapper;
import wakeb.example.microservice.exception.custom.BookAlreadyExistsException;
import wakeb.example.microservice.exception.ExceptionMessagesEnum;
import wakeb.example.microservice.exception.custom.BookNotFoundException;
import wakeb.example.microservice.model.Book;
import wakeb.example.microservice.repository.BookRepository;
import wakeb.example.microservice.service.interfaces.BookCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link BookCommandService} interface that handles command operations
 * (create, update, delete) for books.
 */
@Service
public class BookCommandServiceImpl implements BookCommandService {

    private final BookRepository bookRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param bookRepository the repository for accessing book data.
     */
    @Autowired
    public BookCommandServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Creates a new book after ensuring that it does not already exist.
     *
     * @param bookDTO the data transfer object containing book details.
     * @return the created book as a data transfer object.
     * @throws BookAlreadyExistsException if a book with the same title already exists.
     */
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        // Check if book already exists by title
        if (bookRepository.existsByTitle(bookDTO.getTitle())) {
            throw new BookAlreadyExistsException(
                    ExceptionMessagesEnum.BOOK_ALREADY_EXISTS.getMessage()
            );
        }
        Book book = BookDTOMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return BookDTOMapper.toDTO(savedBook);
    }

    /**
     * Updates an existing book.
     *
     * @param id      the unique identifier of the book to update.
     * @param bookDTO the data transfer object containing updated book details.
     * @return the updated book as a data transfer object.
     * @throws BookNotFoundException if the book with the specified id is not found.
     */
    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        // For simplicity, assume that if it doesn't exist, it fails
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException(ExceptionMessagesEnum.BOOK_NOT_FOUND.getMessage())
                );
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPublicationDate(bookDTO.getPublicationDate());
        Book updatedBook = bookRepository.save(existingBook);
        return BookDTOMapper.toDTO(updatedBook);
    }

    /**
     * Deletes a book by its unique identifier.
     *
     * @param id the unique identifier of the book to delete.
     */
    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}