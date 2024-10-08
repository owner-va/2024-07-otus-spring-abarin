package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

import java.util.List;

public interface MongoBookRepository extends MongoRepository<Book, String> {
    List<Book> findAll();
}
