package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
public class JpaAuthorRepositoryTest {
    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = repository.findById(1L);
        var expectedAuthor = testEntityManager.find(Author.class, 1);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repository.findAll();
        var expectedAuthors = new ArrayList<Author>();
        for (int i = 1; i < 4; i++) {
            expectedAuthors.add(testEntityManager.find(Author.class, i));
        }

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }
}
