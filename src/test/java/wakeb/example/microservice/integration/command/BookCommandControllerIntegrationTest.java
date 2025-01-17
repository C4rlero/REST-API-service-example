package wakeb.example.microservice.integration.command;

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
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookCommandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean the repository before each test
        repository.deleteAll();
    }

    @Test
    void createBook_Success() throws Exception {
        // Given: A valid BookDTO (note: id is null for a new book)
        BookDTO dto = new BookDTO();
        dto.setTitle("Effective Java");
        dto.setAuthor("Joshua Bloch");
        dto.setPublicationDate(LocalDate.of(2018, 1, 1));

        // When & Then: Call POST /api/books and expect 201 Created with returned JSON
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.author", is("Joshua Bloch")))
                .andExpect(jsonPath("$.publicationDate", is("2018-01-01")));

        // Verify: the book is saved in the repository
        Optional<Book> savedBook = repository.findAll().stream().findFirst();
        assert(savedBook.isPresent());
    }

    @Test
    void createBook_Duplicate_ShouldReturnConflict() throws Exception {
        // Given: First, create a book.
        Book book = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1));
        repository.save(book);

        // Then, try to create another book with the same title (assuming title uniqueness is enforced).
        BookDTO dto = new BookDTO();
        dto.setTitle("Clean Code");
        dto.setAuthor("Another Author");
        dto.setPublicationDate(LocalDate.of(2020, 1, 1));

        // When & Then: Expect a 409 Conflict with a ProblemDetail response.
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.title", is("BookAlreadyExistsException")))
                .andExpect(jsonPath("$.detail", containsString("Book already exists")))
                .andExpect(jsonPath("$.errorCode", is("BOOKALREADYEXISTSEXCEPTION")));
    }


    @Test
    void updateBook_Success() throws Exception {
        // Given: Create an initial book in the repository.
        Book book = new Book("Clean Code", "Robert C. Martin", LocalDate.of(2008, 8, 1));
        book = repository.save(book);

        // Prepare updated DTO (id is not needed for update in DTO, as the path variable is used)
        BookDTO updateDto = new BookDTO();
        updateDto.setTitle("Clean Code Updated");
        updateDto.setAuthor("Robert C. Martin");
        updateDto.setPublicationDate(LocalDate.of(2010, 1, 1));

        // When & Then: Call PUT /api/books/{id} and expect 200 OK with updated content.
        mockMvc.perform(put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title", is("Clean Code Updated")))
                .andExpect(jsonPath("$.publicationDate", is("2010-01-01")));

        // Verify: The repository reflects the changes.
        Optional<Book> updatedBook = repository.findById(book.getId());
        assert(updatedBook.isPresent());
        assert(updatedBook.get().getTitle().equals("Clean Code Updated"));
    }

    @Test
    void deleteBook_Success() throws Exception {
        // Given: Create a book to delete.
        Book book = new Book("Domain-Driven Design", "Eric Evans", LocalDate.of(2003, 8, 30));
        book = repository.save(book);

        // When: Call DELETE /api/books/{id} and expect 204 No Content.
        mockMvc.perform(delete("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify: The book is removed from the repository.
        Optional<Book> deletedBook = repository.findById(book.getId());
        assert(deletedBook.isEmpty());
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldReturnNotFound() throws Exception {
        // Given: No book in the repository with id 999.
        Long nonExistentId = 999L;
        BookDTO dto = new BookDTO();
        dto.setTitle("Updated Title");
        dto.setAuthor("Updated Author");
        dto.setPublicationDate(LocalDate.of(2020, 5, 20));

        // When & Then: Expect HTTP 404 Not Found with an error response.
        mockMvc.perform(put("/api/books/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.title", is("BookNotFoundException")))
                .andExpect(jsonPath("$.detail", containsString("Book not found")))
                .andExpect(jsonPath("$.errorCode", is("BOOKNOTFOUNDEXCEPTION")));
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldReturnNoContent() throws Exception {
        // Depending on your implementation, deleting a non-existent book may
        // either return 204 No Content silently or throw an exception.
        // Here, we assume it returns No Content.
        Long nonExistentId = 999L;

        mockMvc.perform(delete("/api/books/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
