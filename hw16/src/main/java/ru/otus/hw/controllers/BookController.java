package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookDtoIds;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books/")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/books/{id}")
    public BookDto getBook(@PathVariable("id") Long id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable("id") Long id, @RequestBody @Valid BookDtoIds bookDtoIds) {
        var book = bookService.update(id,
                bookDtoIds.getTitle(),
                bookDtoIds.getAuthorId(),
                bookDtoIds.getGenresIds());
        return ResponseEntity.ok(book);
    }

    @PostMapping("/api/books/")
    public ResponseEntity<BookDto> saveBook(@RequestBody @Valid BookDtoIds bookDtoIds) {
        var book = bookService.insert(bookDtoIds.getTitle(),
                bookDtoIds.getAuthorId(),
                bookDtoIds.getGenresIds());
        return ResponseEntity.ok(book);
    }
}
