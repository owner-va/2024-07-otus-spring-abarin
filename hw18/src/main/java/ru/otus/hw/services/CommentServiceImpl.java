package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public Optional<CommentDto> findById(long id) {
        return commentRepository.findById(id).map(commentConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public List<CommentDto> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId).stream()
                .map(commentConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public CommentDto insert(String text, long bookId) {
        return commentConverter.toDto(save(0, text, bookId));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public CommentDto update(long id, String text, long bookId) {
        return commentConverter.toDto(save(id, text, bookId));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "serviceCircuitBreaker")
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }
}
