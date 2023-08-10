package de.neuefische.backend;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.neuefische.backend.security.MongoUser;
import de.neuefische.backend.security.MongoUserRepository;
import de.neuefische.backend.todo.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoUserRepository mongoUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setUp() {
        mongoUserRepository.deleteAll();
        mongoUserRepository.save(new MongoUser("123", "user", "password"));
    }

    @Test
    @WithMockUser
    void expectEmptyListOnGet() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    @WithMockUser
    void expectSuccessfulPost() throws Exception {

        String actual = mockMvc.perform(
                        post("http://localhost:8080/api/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"description":"Nächsten Endpunkt implementieren","status":"OPEN"}
                                        """)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "description": "Nächsten Endpunkt implementieren",
                          "status": "OPEN"
                        }
                        """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo actualTodo = objectMapper.readValue(actual, Todo.class);
        assertThat(actualTodo.id())
                .isNotBlank();
    }

    @DirtiesContext
    @Test
    @WithMockUser
    void expectInvalidPost() throws Exception {
        mockMvc.perform(
                        post("http://localhost:8080/api/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"description":"123","status":"OPEN"}
                                        """)
                                .with(csrf())
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                          "message": "Beschreibung muss zwischen 6 und 128 Zeichen lang sein!"
                        }
                        """));
    }

    @DirtiesContext
    @Test
    @WithMockUser
    void expectSuccessfulPut() throws Exception {
        String saveResult = mockMvc.perform(
                        post("http://localhost:8080/api/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"description":"Nächsten Endpunkt implementieren","status":"OPEN"}
                                        """)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "description": "Nächsten Endpunkt implementieren",
                          "status": "OPEN"
                        }
                        """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo saveResultTodo = objectMapper.readValue(saveResult, Todo.class);
        String id = saveResultTodo.id();

        mockMvc.perform(
                        put("http://localhost:8080/api/todo/" + id + "/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"id":"<ID>","description":"Blablablabla","status":"IN_PROGRESS"}
                                        """.replaceFirst("<ID>", id))
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "<ID>",
                          "description": "Blablablabla",
                          "status": "IN_PROGRESS"
                        }
                        """.replaceFirst("<ID>", id)));

    }

    @DirtiesContext
    @Test
    @WithMockUser
    void expectSuccessfulDelete() throws Exception {
        String saveResult = mockMvc.perform(
                        post("http://localhost:8080/api/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"description":"Nächsten Endpunkt implementieren","status":"OPEN"}
                                        """)
                                .with(csrf())
                )
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo saveResultTodo = objectMapper.readValue(saveResult, Todo.class);
        String id = saveResultTodo.id();

        mockMvc.perform(delete("http://localhost:8080/api/todo/" + id)
                        .with(csrf()))
                .andExpect(status().isOk());

        mockMvc.perform(get("http://localhost:8080/api/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        []
                        """));
    }

    @DirtiesContext
    @Test
    @WithMockUser
    void expectTodoOnGetById() throws Exception {
        String actual = mockMvc.perform(
                        post("http://localhost:8080/api/todo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {"description":"Nächsten Endpunkt implementieren","status":"OPEN"}
                                        """)
                                .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "description": "Nächsten Endpunkt implementieren",
                          "status": "OPEN"
                        }
                        """))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Todo actualTodo = objectMapper.readValue(actual, Todo.class);
        String id = actualTodo.id();

        mockMvc.perform(get("http://localhost:8080/api/todo/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "<ID>",
                          "description": "Nächsten Endpunkt implementieren",
                          "status": "OPEN"
                        }
                        """.replaceFirst("<ID>", id)));
    }

    @Test
    @DirtiesContext
    void testLogin() throws Exception {

        mongoUserRepository.save(new MongoUser("123", "frank", passwordEncoder.encode("frank1")));
        //GIVEN
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "frank",
                                "password": "frank1"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testLoginHeader() throws Exception {

        mongoUserRepository.save(new MongoUser("123", "frank", passwordEncoder.encode("frank1")));
        //GIVEN
        mockMvc.perform(get("/api/users/me1")
                        .header(HttpHeaders.AUTHORIZATION, getToken()))
                .andExpect(status().isOk());
    }

    private String getToken() throws Exception {
        return mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "frank",
                        "password": "frank1"
                        }
                        """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }
}
