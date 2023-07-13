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
        TodoStatus status,

        String userId
) {

    Todo(String description, TodoStatus status, String authorId) {
        this(null, description, status, authorId);
    }

    Todo() {
        this(null, null, null, null);
    }


    public Todo withId(String id) {
        return new Todo(id, description, status, userId);
    }

    public Todo withUserId(String authorId) {
        return new Todo(id, description, status, authorId);
    }
}
