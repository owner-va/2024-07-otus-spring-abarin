package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @Test
    public void shouldGivenBooksList() throws Exception {
        given(genreService.findAll())
                .willReturn(getExpectedGenres());
        this.mockMvc.perform(get("/api/genres/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getExpectedGenres())));
    }

    public List<GenreDto> getExpectedGenres() {
        return List.of(
                new GenreDto(1L, "Genre_1"),
                new GenreDto(2L, "Genre_2")
        );
    }
}
