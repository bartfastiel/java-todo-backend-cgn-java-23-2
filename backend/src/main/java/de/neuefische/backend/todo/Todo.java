package de.neuefische.backend.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("todos")
public record Todo(
        @Id
        String id,
        @NotBlank
        @Size(min = 6, max = 128, message = "Beschreibung muss zwischen 6 und 128 Zeichen lang sein!")
        String description,
        @NotNull
        TodoStatus status
) {

    Todo(
            String description,
            TodoStatus status
    ) {
        this(null, description, status);
    }


    public Todo withId(String id) {
        return new Todo(id, description, status);
    }
}
