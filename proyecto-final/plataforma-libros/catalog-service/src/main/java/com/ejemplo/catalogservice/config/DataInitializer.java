package com.ejemplo.catalogservice.config;

import com.ejemplo.catalogservice.model.Book;
import com.ejemplo.catalogservice.repository.BookRepository;
import net.datafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final BookRepository bookRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            if (bookRepository.count() == 0) {
                Faker faker = new Faker(new Locale("en"));
                Random random = new Random();
                String[] genres = {"Fiction", "Romance", "Science", "History", "Fantasy", "Horror"};

                IntStream.range(0, 50).forEach(i -> {
                    Book book = Book.builder()
                            .title(faker.book().title())
                            .author(faker.book().author())
                            .genre(genres[random.nextInt(genres.length)])
                            .price(10 + random.nextDouble() * 40)
                            .build();
                    bookRepository.save(book);
                });
                System.out.println("✅ Libros de prueba generados");
            }
        };
    }
}
