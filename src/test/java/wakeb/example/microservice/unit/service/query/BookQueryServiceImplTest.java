package wakeb.example.microservice.unit.service.query;

import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.exception.custom.BookNotFoundException;
import wakeb.example.microservice.model.Book;
import wakeb.example.microservice.repository.BookRepository;
import wakeb.example.microservice.service.implementation.BookQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookQueryServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookQueryServiceImpl bookQueryService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        // Setup sample book entities
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setPublicationDate(LocalDate.of(2018, 1, 1));

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setPublicationDate(LocalDate.of(2008, 8, 1));
    }

    @Test
    void findBookById_WhenBookExists_ShouldReturnDto() {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        // WHEN
        BookDTO result = bookQueryService.findBookById(1L);

        // THEN
        verify(bookRepository, times(1)).findById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Effective Java");
        assertThat(result.getAuthor()).isEqualTo("Joshua Bloch");
        assertThat(result.getPublicationDate()).isEqualTo(LocalDate.of(2018, 1, 1));
    }

    @Test
    void findBookById_WhenBookDoesNotExist_ShouldThrowBookNotFoundException() {
        // GIVEN
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> bookQueryService.findBookById(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found");

        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    void findAllBooks_WhenBooksExist_ShouldReturnListOfBookDTO() {
        // GIVEN
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // WHEN
        List<BookDTO> result = bookQueryService.findAllBooks();

        // THEN
        verify(bookRepository, times(1)).findAll();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isIn("Effective Java", "Clean Code");
        assertThat(result.get(1).getTitle()).isIn("Effective Java", "Clean Code");
    }

    @Test
    void findAllBooks_WhenNoBooksExist_ShouldReturnEmptyList() {
        // GIVEN
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        List<BookDTO> result = bookQueryService.findAllBooks();

        // THEN
        verify(bookRepository, times(1)).findAll();
        assertThat(result).isEmpty();
    }
}