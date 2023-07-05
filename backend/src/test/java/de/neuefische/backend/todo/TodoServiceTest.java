package de.neuefische.backend.todo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;

class TodoServiceTest {

    TodoRepository todoRepository = mock(TodoRepository.class);

    TodoService todoService = new TodoService(todoRepository);

    @Test
    void getAllCallsRepository() {
        // given
        Todo testItem = new Todo("bla", TodoStatus.OPEN);
        Mockito.when(todoRepository.findAll())
                .thenReturn(Collections.singletonList(testItem));

        // when
        List<Todo> actual = todoService.getAll();

        // then
        Assertions.assertThat(actual)
                .containsExactly(testItem);
    }

}






