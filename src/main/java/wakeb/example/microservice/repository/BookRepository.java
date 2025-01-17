package wakeb.example.microservice.repository;
import wakeb.example.microservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for performing CRUD operations on {@link Book} entities.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Checks if a book exists by its title.
     *
     * @param title the title of the book.
     * @return {@code true} if a book with the specified title exists, {@code false} otherwise.
     */
    boolean existsByTitle(String title);
}