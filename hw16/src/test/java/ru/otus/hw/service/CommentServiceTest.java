package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис комментариев")
@DataJpaTest
@Import({CommentServiceImpl.class,
        CommentConverter.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceTest {
    @Autowired
    private CommentService service;

    @Test
    @DisplayName("должен загружать комментарий по id")
    void shouldReturnCorrectCommentById() {
        CommentDto expectedComment = new CommentDto(1L, "Great book, really enjoyed it!", 1L);
        var actualComment = service.findById(1);
        assertThat(actualComment).isPresent()
                .get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен загружать список комментариев исходя из id книги")
    void shouldReturnCorrectCommentsList() {
        var actualComments = service.findByBookId(1);
        var expectedComments = List.of(
                new CommentDto(1L, "Great book, really enjoyed it!", 1L),
                new CommentDto(2L, "Very informative and well-written.", 1L),
                new CommentDto(3L, "Not my cup of tea.", 1L)
        );
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("должен сохранять новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveNewComment() {
        var expectedComment = new CommentDto(0L, "NewComment", 1L);
        var actualComment = service.insert("NewComment", 1);
        assertThat(actualComment)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedComment);
        assertThat(actualComment.getId()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("должен сохранять измененный комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldSaveUpdatedBook() {
        var expectedComment = new CommentDto(1L, "UpdatedBookComment", 1L);
        var beforeUpdate = service.findById(1);
        assertThat(beforeUpdate).isPresent().get().isNotEqualTo(expectedComment);

        service.update(1, "UpdatedBookComment", 1);
        var afterUpdate = service.findById(1);

        assertThat(afterUpdate).isPresent().get().isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий по id ")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        assertThat(service.findById(1L)).isPresent();
        service.deleteById(1L);
        assertThat(service.findById(1L)).isEmpty();
    }
}
