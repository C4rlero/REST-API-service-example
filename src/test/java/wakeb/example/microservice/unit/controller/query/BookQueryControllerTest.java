package wakeb.example.microservice.unit.controller.query;

import org.springframework.test.context.ContextConfiguration;
import wakeb.example.microservice.Application;
import wakeb.example.microservice.controller.BookQueryController;
import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.exception.handler.GlobalExceptionHandler;
import wakeb.example.microservice.service.interfaces.BookQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookQueryController.class)
@ContextConfiguration(classes = {Application.class, BookQueryController.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = "api.endpoint.books=/api/books")
class BookQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookQueryService bookQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO book1;
    private BookDTO book2;

    @BeforeEach
    void setUp() {
        // Setup sample DTOs for testing
        book1 = new BookDTO(1L, "Effective Java", "Joshua Bloch", LocalDate.of(2018, 1, 1));
        book2 = new BookDTO(2L, "Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1));
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnOkAndBookDto() throws Exception {
        // GIVEN
        when(bookQueryService.findBookById(eq(1L))).thenReturn(book1);

        // WHEN
        ResultActions result = mockMvc.perform(get("/api/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.publicationDate").value("2018-01-01"));

        verify(bookQueryService, times(1)).findBookById(1L);
    }

    @Test
    void getAllBooks_WhenBooksExist_ShouldReturnOkAndListOfBooks() throws Exception {
        // GIVEN
        List<BookDTO> books = Arrays.asList(book1, book2);
        when(bookQueryService.findAllBooks()).thenReturn(books);

        // WHEN
        ResultActions result = mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Effective Java"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Clean Code"));

        verify(bookQueryService, times(1)).findAllBooks();
    }

    @Test
    void getAllBooks_WhenNoBooksExist_ShouldReturnOkAndEmptyList() throws Exception {
        // GIVEN
        when(bookQueryService.findAllBooks()).thenReturn(Collections.emptyList());

        // WHEN
        ResultActions result = mockMvc.perform(get("/api/books")
                .contentType(MediaType.APPLICATION_JSON));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookQueryService, times(1)).findAllBooks();
    }
}