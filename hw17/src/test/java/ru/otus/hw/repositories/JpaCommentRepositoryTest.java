package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
public class JpaCommentRepositoryTest {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("должен загружать комментарий по id")
    void shouldReturnCorrectCommentById() {
        var actualComment = repository.findById(1L);
        var expectedComment = testEntityManager.find(Comment.class, 1);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен загружать список комментариев исходя из id книги")
    void shouldReturnCorrectCommentsList() {
        var actualComments = repository.findByBookId(1L);
        var expectedComments = List.of(
                testEntityManager.find(Comment.class, 1),
                testEntityManager.find(Comment.class, 2),
                testEntityManager.find(Comment.class, 3)
        );
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("должен сохранять новый комментарий")
    void shouldSaveNewComment() {
        Book book = testEntityManager.find(Book.class, 1);
        var expectedComment = new Comment(0, "newComment", book);
        var returnedComment = repository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, returnedComment.getId()))
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен сохранять измененный комментарий")
    void shouldSaveUpdatedBook() {
        Book book = testEntityManager.find(Book.class, 1);

        var expectedComment = new Comment(1, "UpdatedBook", book);
        assertThat(testEntityManager.find(Comment.class, expectedComment.getId()))
                .isNotEqualTo(expectedComment);

        var returnedComment = repository.save(expectedComment);
        assertThat(expectedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(testEntityManager.find(Comment.class, returnedComment.getId()))
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("должен удалять комментарий по id ")
    void shouldDeleteBook() {
        assertThat(testEntityManager.find(Comment.class, 1L)).isNotNull();
        repository.deleteById(1L);
        assertThat(testEntityManager.find(Comment.class, 1L)).isNull();
    }
}
