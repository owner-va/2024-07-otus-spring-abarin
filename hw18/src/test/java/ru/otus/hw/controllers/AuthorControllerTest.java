package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @Test
    public void shouldGivenBooksList() throws Exception {
        given(authorService.findAll())
                .willReturn(getExpectedAuthors());
        this.mockMvc.perform(get("/api/authors/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getExpectedAuthors())));
    }

    public List<AuthorDto> getExpectedAuthors() {
        return List.of(
                new AuthorDto(1L, "Author_1"),
                new AuthorDto(2L, "Author_2"),
                new AuthorDto(3L, "Author_3")
        );
    }
}
