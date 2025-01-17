package wakeb.example.microservice.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/**
 * Entity representing a Book in the system.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private LocalDate publicationDate;

    //Default constructors
    public Book() {}

    /**
     * Constructs a new Book with the specified title, author, and publication date.
     *
     * @param title           the title of the book.
     * @param author          the author of the book.
     * @param publicationDate the publication date of the book.
     */
    public Book(String title, String author, LocalDate publicationDate) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
    }
}