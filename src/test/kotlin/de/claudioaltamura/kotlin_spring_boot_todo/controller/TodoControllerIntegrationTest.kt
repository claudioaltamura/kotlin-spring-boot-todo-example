package de.claudioaltamura.kotlin_spring_boot_todo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.claudioaltamura.kotlin_spring_boot_todo.dto.ApplicationError
import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import de.claudioaltamura.kotlin_spring_boot_todo.repository.TodoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TodoControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        todoRepository.deleteAll()
    }

    @Test
    fun `should add a todo successfully`() {
        //given
        val newTodo = NewTodo("a todo", "this is a todo.")

        //when
        val createdToDo = webTestClient
            .post()
            .uri("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newTodo)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        todoRepository.findById(createdToDo!!.id).orElseThrow { TodoNotFoundException("Todo not found") }
        assertThat(createdToDo).isNotNull
    }

    @Test
    fun `should return a bad request when given a faulty todo`() {
        val applicationError = webTestClient
            .post()
            .uri("/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString("{\"title\": \"\", \"description\": \"todo\"}"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ApplicationError::class.java)
            .returnResult()
            .responseBody

        assertThat(applicationError!!.message).contains("JSON parse error: Cannot construct instance of `de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value")
    }

    @Test
    fun `should return todos find by title`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))

        //when
        val todoList = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos").queryParam("title", todo.title).build() }
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(todoList!!.size).isEqualTo(1)
    }

    @Test
    fun `should return a bad request error when not title parameter given`() {
        webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos").build() }
            .exchange()
            .expectStatus().isBadRequest
            .expectBodyList(ApplicationError::class.java)
    }

    @Test
    fun `should return a todo when id given`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))

        //when
        val createdTodo = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(todo.id)}
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(createdTodo!!.id).isEqualTo(todo.id)
    }

    @Test
    fun `should return a not found error when id given`() {
        val noExistingTodo = 999L
        webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(noExistingTodo) }
            .exchange()
            .expectStatus().isNotFound
            .expectBody(ApplicationError::class.java)
    }

    @Test
    fun `should update a todo when found`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))

        //when
        val updatedTodo = Todo(todo.id!!, "todo", "changed description")
        val changedTodo = webTestClient.put()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(updatedTodo.id) }
            .bodyValue(updatedTodo)
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        val todoFromDb = todoRepository.findById(changedTodo!!.id).orElseThrow { TodoNotFoundException("Todo not found") }
        assertThat(todoFromDb.description).isEqualTo("changed description")
        assertThat(changedTodo.description).isEqualTo("changed description")
    }

    @Test
    fun `should delete a todo when found`() {
        //given
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))

        //when
        webTestClient.delete()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(todo.id) }
            .exchange()
            .expectStatus().isNoContent

        //then
        assertThat(todoRepository.existsById(todo.id!!)).isFalse
    }

}