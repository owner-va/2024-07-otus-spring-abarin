package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис книг")
@DataJpaTest
@Import({BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("должен возвращать все книги")
    void shouldReturnCorrectBooksList() {
        var actualBook = bookService.findAll();
        assertThat(actualBook).isEqualTo(getExpectedBooks());
    }

    @Test
    @DisplayName("должен возвращать книгу по id")
    void shouldReturnCorrectBookById() {
        BookDto byId = bookService.findById(1);
        assertThat(byId).isEqualTo(getExpectedBooks().get(0));
    }

    @Test
    @DisplayName("должен добавлять новую книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewBook() {
        var author = new AuthorDto(1L, "Author_1");
        var genre = new GenreDto(2L, "Genre_2");
        var expectedBook = new BookDto(4L, "BookTitle_10500", author,
                List.of(genre));
        BookDto actualBook = bookService.insert("BookTitle_10500", 1, Set.of(2L));
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("должен изменять книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateBook() {
        var beforeUpdate = bookService.findById(1);

        var updated = bookService.update(1, "BookTitle_Updated", 1, Set.of(2L));
        assertThat(beforeUpdate).isNotEqualTo(updated);

        var afterUpdate = bookService.findById(1);
        assertThat(afterUpdate.getTitle()).isEqualTo("BookTitle_Updated");
    }

    @Test
    @DisplayName("должен удалять книгу")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        bookService.deleteById(1);
        var actualBooks = bookService.findAll();
        assertThat(actualBooks).doesNotContain(getExpectedBooks().get(0));
        assertThat(actualBooks).containsExactly(getExpectedBooks().get(1), getExpectedBooks().get(2));
    }

    public List<BookDto> getExpectedBooks() {
        return List.of(
                new BookDto(1L, "BookTitle_1",
                        new AuthorDto(1L, "Author_1"),
                        List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L, "Genre_2"))),
                new BookDto(2L, "BookTitle_2",
                        new AuthorDto(2L, "Author_2"),
                        List.of(new GenreDto(3L, "Genre_3"), new GenreDto(4L, "Genre_4"))),
                new BookDto(3L, "BookTitle_3",
                        new AuthorDto(3L, "Author_3"),
                        List.of(new GenreDto(5L, "Genre_5"), new GenreDto(6L, "Genre_6")))
        );
    }
}
