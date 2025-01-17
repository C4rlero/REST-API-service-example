package wakeb.example.microservice.service.implementation;

import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.dto.book.BookDTOMapper;
import wakeb.example.microservice.exception.custom.BookNotFoundException;
import wakeb.example.microservice.exception.ExceptionMessagesEnum;
import wakeb.example.microservice.model.Book;
import wakeb.example.microservice.repository.BookRepository;
import wakeb.example.microservice.service.interfaces.BookQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Implementation of the {@link BookQueryService} interface that handles query operations
 * for retrieving book information.
 */
@Service
public class BookQueryServiceImpl implements BookQueryService {

    private final BookRepository bookRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param bookRepository the repository for accessing book data.
     */
    @Autowired
    public BookQueryServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id the unique identifier of the book.
     * @return the book as a data transfer object.
     * @throws BookNotFoundException if the book with the specified id is not found.
     */
    @Override
    public BookDTO findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(
                        ExceptionMessagesEnum.BOOK_NOT_FOUND.getMessage()
                ));
        return BookDTOMapper.toDTO(book);
    }


    /**
     * Retrieves all books.
     *
     * @return a list of data transfer objects representing all books.
     */
    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTOMapper::toDTO)
                .collect(Collectors.toList());
    }
}