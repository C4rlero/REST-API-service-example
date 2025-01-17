package wakeb.example.microservice.unit.controller.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ContextConfiguration;
import wakeb.example.microservice.Application;
import wakeb.example.microservice.controller.BookCommandController;
import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.exception.handler.GlobalExceptionHandler;
import wakeb.example.microservice.service.interfaces.BookCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;


@WebMvcTest(controllers = BookCommandController.class)
@ContextConfiguration(classes = {Application.class, BookCommandController.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {
        "api.endpoint.books=/api/books" // if you have custom properties
})
class BookCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCommandService bookCommandService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO validDto;

    @BeforeEach
    void setUp() {
        // A valid DTO
        validDto = new BookDTO();
        validDto.setId(100L);
        validDto.setTitle("Effective Java");
        validDto.setAuthor("Joshua Bloch");
        validDto.setPublicationDate(LocalDate.of(2018, 1, 1));
    }

    @Test
    void createBook_WhenValidRequest_ShouldReturnBookDtoAndStatusCreated() throws Exception {
        // GIVEN
        BookDTO savedDto = new BookDTO(
                100L,
                "Effective Java",
                "Joshua Bloch",
                LocalDate.of(2018, 1, 1)
        );
        when(bookCommandService.createBook(any(BookDTO.class))).thenReturn(savedDto);

        // WHEN
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                // THEN
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"));

        verify(bookCommandService, times(1)).createBook(any(BookDTO.class));
    }

    @Test
    void createBook_WhenInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // GIVEN: an invalid DTO (blank title and author, and no publicationDate)
        BookDTO invalidDto = new BookDTO();
        invalidDto.setId(101L);
        invalidDto.setTitle("");  // violates @NotBlank
        invalidDto.setAuthor(""); // violates @NotBlank
        // publicationDate might be optional depending on your validations. If it's required, set it to null.

        // WHEN & THEN
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                // Since a Problem Detail is returned, we can check:
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                // Depending on your GlobalExceptionHandler, the detail may vary.
                .andExpect(jsonPath("$.detail", containsString("Invalid request content")))
                .andExpect(jsonPath("$.instance", is("/api/books")));

        verify(bookCommandService, times(0)).createBook(any(BookDTO.class));
    }

    @Test
    void updateBook_WhenValidRequest_ShouldReturnOk() throws Exception {
        // GIVEN
        Long bookId = 100L;
        BookDTO updateDto = new BookDTO(bookId, "Updated Title", "Updated Author", LocalDate.of(2020, 5, 20));
        when(bookCommandService.updateBook(eq(bookId), any(BookDTO.class))).thenReturn(updateDto);

        // WHEN
        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                // THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"));

        verify(bookCommandService, times(1)).updateBook(eq(bookId), any(BookDTO.class));
    }

    @Test
    void deleteBook_WhenValidId_ShouldReturnNoContent() throws Exception {
        Long bookId = 100L;
        // No need to stub the service method for void methods unless it throws an exception.

        // WHEN
        mockMvc.perform(delete("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());

        verify(bookCommandService, times(1)).deleteBook(bookId);
    }

}