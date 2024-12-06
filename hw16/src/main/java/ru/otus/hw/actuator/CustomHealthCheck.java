package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;

@Component
@RequiredArgsConstructor
public class CustomHealthCheck implements HealthIndicator {

    private final AuthorService authorService;

    @Override
    public Health health() {
        if (authorService.count() > 0) {
            return Health.up().withDetail("message", "Авторы не пусты, можно работать").build();
        }
        return Health.down()
                .status(Status.DOWN)
                .withDetail("message", "Караул, авторы пустые!")
                .build();
    }
}
