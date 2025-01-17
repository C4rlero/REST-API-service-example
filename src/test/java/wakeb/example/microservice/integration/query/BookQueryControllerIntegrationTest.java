package wakeb.example.microservice.integration.query;

import wakeb.example.microservice.Application;
import wakeb.example.microservice.dto.book.BookDTO;
import wakeb.example.microservice.model.Book;
import wakeb.example.microservice.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        // Ensure repository is empty before each test.
        repository.deleteAll();

        // Create sample books.
        book1 = new Book("Effective Java", "Joshua Bloch", LocalDate.of(2018, 1, 1));
        book2 = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1));

        // Persist the test data.
        repository.saveAll(Arrays.asList(book1, book2));
    }

    @Test
    void getBookById_Success() throws Exception {
        // When: Call GET /api/books/{id} with book1's id.
        mockMvc.perform(get("/api/books/{id}", book1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                // Then: Expect 200 OK and verify JSON response.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(book1.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.author", is("Joshua Bloch")))
                .andExpect(jsonPath("$.publicationDate", is("2018-01-01")));
    }

    @Test
    void getAllBooks_Success() throws Exception {
        // When: Call GET /api/books.
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then: Expect 200 OK and verify that the list contains both books.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[?(@.title=='Effective Java')]", hasSize(1)))
                .andExpect(jsonPath("$[?(@.title=='Clean Code')]", hasSize(1)));
    }

    @Test
    void createBook_Duplicate_ShouldReturnConflict() throws Exception {
        // Given: First, create a book.
        Book book = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1));
        repository.save(book);

        // Then, try to create another book with the same title.
        BookDTO dto = new BookDTO();
        dto.setTitle("Clean Code");
        dto.setAuthor("Another Author");
        dto.setPublicationDate(LocalDate.of(2020, 1, 1));

        // When & Then: Expect a 409 Conflict.
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                // Expect the title to be "BookAlreadyExistsException" if that is what the exception handler returns.
                .andExpect(jsonPath("$.title", is("BookAlreadyExistsException")))
                .andExpect(jsonPath("$.detail", containsString("Book already exists")))
                .andExpect(jsonPath("$.errorCode", is("BOOKALREADYEXISTSEXCEPTION")));
    }

}