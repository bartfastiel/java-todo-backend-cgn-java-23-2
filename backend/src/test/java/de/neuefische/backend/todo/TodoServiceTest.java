package de.neuefische.backend.todo;

import de.neuefische.backend.security.MongoUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

class TodoServiceTest {

    TodoRepository todoRepository = mock(TodoRepository.class);
    MongoUserService mongoUserService = mock(MongoUserService.class);

    TodoService todoService = new TodoService(todoRepository, mongoUserService);

    @Test
    void getAllCallsRepository() {
        // given
        Todo testItem = new Todo("bla", TodoStatus.OPEN, "test-author-id");
        Mockito.when(todoRepository.findAll())
                .thenReturn(Collections.singletonList(testItem));

        // when
        List<Todo> actual = todoService.getAll();

        // then
        Assertions.assertThat(actual)
                .containsExactly(testItem);
    }

}






