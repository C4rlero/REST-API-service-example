package wakeb.example.microservice.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 100, message = "Author must not exceed 100 characters")
    private String author;

    private LocalDate publicationDate;

    public BookDTO() {}

    public BookDTO(Long id, String title, String author, LocalDate publicationDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
    }
}