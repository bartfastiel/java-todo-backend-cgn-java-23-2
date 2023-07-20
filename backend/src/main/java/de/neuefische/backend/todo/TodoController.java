package de.neuefische.backend.todo;

import de.neuefische.backend.exception.ErrorMessage;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/todo")
class TodoController {

    private final TodoService todoService;

    TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    List<Todo> getAll() {
        return todoService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo postTodo(@Valid @RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @GetMapping("{id}")
    Todo getTodoById(@PathVariable String id) {
        return todoService.getById(id);
    }

    @PutMapping(path = {"{id}/update", "{id}"})
    ResponseEntity<Todo> update(@PathVariable String id, @Valid @RequestBody Todo todo) {
        if (!todo.id().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The id in the url does not match the request body's id");
        }
        return new ResponseEntity<>(todoService.update(todo), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    void delete(@PathVariable String id) {
        todoService.delete(id);
    }



    @ExceptionHandler({NoSuchElementException.class, })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNoSuchElementExceptions(NoSuchElementException exception) {
        return new ErrorMessage(exception.getMessage() + " LOKAL");
    }
}






