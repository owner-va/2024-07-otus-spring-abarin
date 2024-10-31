package ru.otus.hw.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "genres")
public class Genre {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}
