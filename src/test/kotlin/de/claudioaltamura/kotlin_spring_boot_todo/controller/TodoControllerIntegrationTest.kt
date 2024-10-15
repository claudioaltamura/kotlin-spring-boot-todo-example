package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import de.claudioaltamura.kotlin_spring_boot_todo.service.TodoService
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    lateinit var todoService: TodoService

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
        assertThat(createdToDo).isNotNull
    }

    @Test
    fun `should return todos find by title`() {
        //given
        todoService.addTodo(NewTodo("a todo", "more details..."))

        //when
        val todoList = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos").queryParam("title", "todo").build() }
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(todoList!!.size).isEqualTo(1)
    }

    @Test
    fun `should return a todo when id given`() {
        //given
        todoService.addTodo(NewTodo("a todo", "more details..."))

        //when
        val todo = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(1) }
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(todo!!.id).isEqualTo(1)
    }

    @Test
    fun `should update a todo when found`() {
        //given
        val addedTodo = todoService.addTodo(NewTodo("a todo", "this is a todo."))

        //when
        val updatedTodo = Todo(addedTodo.id, "a todo", "changed description")
        val changedTodo = webTestClient.put()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(addedTodo.id) }
            .bodyValue(updatedTodo)
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(changedTodo!!.description).isEqualTo("changed description")
    }

    @Test
    fun `should delete a todo when found`() {
        //given
        val addedTodo = todoService.addTodo(NewTodo("a todo", "this is a todo."))

        //when
        webTestClient.delete()
            .uri { uriBuilder -> uriBuilder.path("/todos/{id}").build(addedTodo.id) }
            .exchange()
            .expectStatus().isNoContent

        //then
        assertThrows<TodoNotFoundException> { todoService.getTodo(addedTodo.id) }


    }

}