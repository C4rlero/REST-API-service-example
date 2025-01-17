package wakeb.example.microservice.dto.book;

import wakeb.example.microservice.model.Book;

/**
 * Utility class for converting between {@link Book} entities and {@link BookDTO} data transfer objects.
 */
public class BookDTOMapper {

    /**
     * Converts a {@link Book} entity to a {@link BookDTO}.
     *
     * @param entity the Book entity to convert.
     * @return the corresponding BookDTO, or {@code null} if the entity is {@code null}.
     */
    public static BookDTO toDTO(Book entity) {
        if (entity == null) return null;
        BookDTO dto = new BookDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublicationDate(entity.getPublicationDate());
        return dto;
    }

    /**
     * Converts a {@link BookDTO} to a {@link Book} entity.
     *
     * @param dto the BookDTO to convert.
     * @return the corresponding Book entity, or {@code null} if the dto is {@code null}.
     */
    public static Book toEntity(BookDTO dto) {
        if (dto == null) return null;
        Book entity = new Book();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublicationDate(dto.getPublicationDate());
        return entity;
    }
}