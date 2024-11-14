package de.claudioaltamura.kotlin_spring_boot_todo.controller

import com.ninjasquad.springmockk.MockkBean
import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.service.TodoService
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(TodoController::class)
@AutoConfigureWebTestClient
class TodoControllerTest {

  @Autowired lateinit var webTestClient: WebTestClient

  @MockkBean lateinit var todoService: TodoService

  @Test
  fun `should add a todo successfully`() {
    val newTodo = NewTodo("a todo", "this is a todo.")

    every { todoService.addTodo(any()) }.returns(Todo(1L, "a todo", "this is a todo."))

    val createdToDo =
      webTestClient
        .post()
        .uri("/todos")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newTodo)
        .exchange()
        .expectStatus()
        .isCreated
        .expectBody(Todo::class.java)
        .returnResult()
        .responseBody

    assertThat(createdToDo!!.id).isEqualTo(1L)

    verify { todoService.addTodo(any()) }
  }

  @Test
  fun `should return todos find by title`() {
    every { todoService.getTodos(any()) }.returns(listOf(Todo(1L, "a todo", "this is a todo.")))

    val todoList =
      webTestClient
        .get()
        .uri { uriBuilder -> uriBuilder.path("/todos").queryParam("title", "todo").build() }
        .exchange()
        .expectStatus()
        .isOk
        .expectBodyList(Todo::class.java)
        .returnResult()
        .responseBody

    assertThat(todoList!!.size).isEqualTo(1)
  }

  @Test
  fun `should return a todo when id given`() {
    every { todoService.getTodo(any()) }.returns(Todo(1L, "a todo", "this is a todo."))

    val todo =
      webTestClient
        .get()
        .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(1L) }
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(Todo::class.java)
        .returnResult()
        .responseBody

    assertThat(todo!!.id).isEqualTo(1L)

    verify { todoService.getTodo(any()) }
  }

  @Test
  fun `should update a todo when found`() {
    val updatedTodo = Todo(1L, "a todo", "changed description")

    every { todoService.updateTodo(any(), any()) }.returns(updatedTodo)

    val todo =
      webTestClient
        .put()
        .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(1L) }
        .bodyValue(updatedTodo)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody(Todo::class.java)
        .returnResult()
        .responseBody

    assertThat(todo!!.description).isEqualTo("changed description")

    verify { todoService.updateTodo(any(), any()) }
  }

  @Test
  fun `should delete a todo when found`() {
    justRun { todoService.deleteTodo(any()) }

    webTestClient
      .delete()
      .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(1L) }
      .exchange()
      .expectStatus()
      .isNoContent

    verify(exactly = 1) { todoService.deleteTodo(any()) }
  }
}
