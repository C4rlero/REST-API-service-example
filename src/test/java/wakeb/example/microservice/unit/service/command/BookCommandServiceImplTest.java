package wakeb.example.microservice.unit.service.command;


import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.exception.ExceptionMessagesEnum;
import wakeb.example.microservice.exception.custom.BookAlreadyExistsException;
import wakeb.example.microservice.service.implementation.BookCommandServiceImpl;
import wakeb.example.microservice.model.Book;
import wakeb.example.microservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCommandServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookCommandServiceImpl bookCommandService;

    private BookDTO validDto;
    private Book bookEntity;

    @BeforeEach
    void setUp() {
        validDto = new BookDTO();
        validDto.setId(null);
        validDto.setTitle("Clean Code");
        validDto.setAuthor("Robert C. Martin");
        validDto.setPublicationDate(LocalDate.of(2008, 8, 1));

        bookEntity = new Book();
        bookEntity.setId(1L);
        bookEntity.setTitle("Clean Code");
        bookEntity.setAuthor("Robert C. Martin");
        bookEntity.setPublicationDate(LocalDate.of(2008, 8, 1));
    }

    @Test
    void createBook_WhenBookDoesNotExist_ShouldSaveAndReturnDto() {
        // GIVEN
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(bookEntity);

        // WHEN
        BookDTO result = bookCommandService.createBook(validDto);

        // THEN
        verify(bookRepository, times(1)).existsByTitle("Clean Code");
        verify(bookRepository, times(1)).save(any(Book.class));
        assertThat(result.getTitle()).isEqualTo("Clean Code");
        assertThat(result.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void createBook_WhenBookAlreadyExists_ShouldThrowBookAlreadyExistsException() {
        // GIVEN
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(true);

        // WHEN & THEN
        assertThatThrownBy(() -> bookCommandService.createBook(validDto))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage(ExceptionMessagesEnum.BOOK_ALREADY_EXISTS.getMessage());

        verify(bookRepository, times(1)).existsByTitle("Clean Code");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookFound_ShouldUpdateAndReturnDto() {
        // GIVEN: let's say we want to update ID 1 with new data
        BookDTO updateDto = new BookDTO(null, "Updated Title", "Updated Author", LocalDate.of(2020, 1, 1));
        Book existingBook = new Book("Old Title", "Old Author", LocalDate.of(2010, 1, 1));
        existingBook.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenAnswer(inv -> {
            existingBook.setTitle("Updated Title");
            existingBook.setAuthor("Updated Author");
            existingBook.setPublicationDate(LocalDate.of(2020, 1, 1));
            return existingBook;
        });

        // WHEN
        BookDTO result = bookCommandService.updateBook(1L, updateDto);

        // THEN
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
        assertThat(result.getPublicationDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldThrowRuntimeException() {
        // You might throw BookNotFoundException in a real scenario
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookCommandService.updateBook(99L, validDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Book not found");

        verify(bookRepository, times(1)).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldCallRepositoryDelete() {
        // WHEN
        bookCommandService.deleteBook(1L);

        // THEN
        verify(bookRepository, times(1)).deleteById(1L);
    }
}

