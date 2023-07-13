package de.neuefische.backend.todo;

import de.neuefische.backend.security.MongoUser;
import de.neuefische.backend.security.MongoUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class TodoService {

    private final TodoRepository todoRepository;
    private final MongoUserService mongoUserService;

    TodoService(TodoRepository todoRepository, MongoUserService mongoUserService) {
        this.todoRepository = todoRepository;
        this.mongoUserService = mongoUserService;
    }

    List<Todo> getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUser user = mongoUserService.findUserByUsername(username);

        return todoRepository.findAllByUserId(user.id());
    }

    public Todo save(Todo todo) {
        String id = UUID.randomUUID().toString();

        Todo todoWithId = todo.withId(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MongoUser user = mongoUserService.findUserByUsername(username);

        Todo todoToSave = new Todo(todoWithId.id(), todoWithId.description(), todoWithId.status(), user.id());

        return todoRepository.save(todoToSave);
    }

    public Todo getById(String id) {
        return todoRepository.findById(id).orElseThrow();
    }

    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    public void delete(String id) {
        todoRepository.deleteById(id);
    }
}





