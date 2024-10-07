package de.claudioaltamura.kotlin_spring_boot_todo.controller

import com.ninjasquad.springmockk.MockkBean
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.service.TodoService
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(TodoController::class)
@AutoConfigureWebTestClient
class TodoControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var todoService: TodoService

    @Test
    fun getTodo() {
        every { todoService.getTodo(any()) }.returns(Todo(1, "a todo", "this is a todo."))

        val todo = webTestClient.get()
            .uri("/todos/1")
            .exchange()
            .expectStatus().isOk
            .expectBody(Todo::class.java)
            .returnResult()
            .responseBody

        assertThat(todo!!.id).isEqualTo(1)

        verify { todoService.getTodo(any()) }
    }
}