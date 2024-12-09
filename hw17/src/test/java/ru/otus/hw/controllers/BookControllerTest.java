package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoIds;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @Test
    public void shouldGivenBooksList() throws Exception {
        given(bookService.findAll())
                .willReturn(getExpectedBooks());
        this.mockMvc.perform(get("/api/books/"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getExpectedBooks())));
    }


    @Test
    public void shouldGivenBook() throws Exception {
        given(bookService.findById(1))
                .willReturn(getExpectedBooks().get(0));
        this.mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getExpectedBooks().get(0))));
        verify(bookService, times(1)).findById(1);
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1);
        this.mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(1);
    }


    @Test
    public void shouldUpdateBookAndReturn() throws Exception {
        var bookDtoIds = new BookDtoIds("New Title", 2L, Set.of(3L));
        var genreDto = new GenreDto(3L, "Genre_3");
        var authorDto = new AuthorDto(2L, "Author_2");
        var bookId = 1L;
        var updatedBook = new BookDto(bookId, "New Title", authorDto, List.of(genreDto));

        when(bookService.update(eq(bookId), anyString(), anyLong(), anySet()))
                .thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookDtoIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedBook)));
    }

    @Test
    public void shouldSaveBookAndReturn() throws Exception {
        var bookDtoIds = new BookDtoIds("New Title", 2L, Set.of(3L));
        var genreDto = new GenreDto(3L, "Genre_3");
        var authorDto = new AuthorDto(2L, "Author_2");
        var savedBook = new BookDto(1L, "New Title", authorDto, List.of(genreDto));

        when(bookService.insert(anyString(), anyLong(), anySet()))
                .thenReturn(savedBook);

        mockMvc.perform(post("/api/books/")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookDtoIds)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(savedBook)));
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
