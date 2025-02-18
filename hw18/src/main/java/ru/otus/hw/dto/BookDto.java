package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto implements Serializable {
    private Long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}