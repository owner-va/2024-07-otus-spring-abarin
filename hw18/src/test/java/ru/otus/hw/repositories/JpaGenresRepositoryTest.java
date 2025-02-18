package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
public class JpaGenresRepositoryTest {
    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("должен загружать жанры по ids")
    @Test
    void shouldReturnCorrectGenresById() {
        var ids = Set.of(1L, 2L, 3L);
        var actualGenres = repository.findAllById(ids);
        var expectedGenres = List.of(
                testEntityManager.find(Genre.class, 1),
                testEntityManager.find(Genre.class, 2),
                testEntityManager.find(Genre.class, 3)
        );
        assertThat(actualGenres).isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать список всех жарнов")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repository.findAll();
        var expectedGenres = new ArrayList<Genre>();
        for (int i = 1; i < 7; i++) {
            expectedGenres.add(testEntityManager.find(Genre.class, i));
        }

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }
}
