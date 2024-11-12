package de.claudioaltamura.kotlin_spring_boot_todo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.claudioaltamura.kotlin_spring_boot_todo.dto.*
import de.claudioaltamura.kotlin_spring_boot_todo.entity.CommentEntity
import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import de.claudioaltamura.kotlin_spring_boot_todo.repository.CommentRepository
import de.claudioaltamura.kotlin_spring_boot_todo.repository.TodoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CommentControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var todoRepository: TodoRepository

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Test
    fun `should add a comment successfully`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))
        val newComment = NewComment("a comment", todo.id!!)

        //when
        val createdComment = webTestClient
            .post()
            .uri("/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newComment)
            .exchange()
            .expectStatus().isCreated
            .expectBody(Comment::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(createdComment).isNotNull
    }

    @Test
    fun `should return a bad request when given a faulty comment`() {
        val applicationError = webTestClient
            .post()
            .uri("/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString("{\"text\": \"\", \"todo\": 1L}"))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ApplicationError::class.java)
            .returnResult()
            .responseBody

        assertThat(applicationError!!.message).contains("JSON parse error: Cannot construct instance of `de.claudioaltamura.kotlin_spring_boot_todo.dto.NewComment` (although at least one Creator exists): no String-argument constructor/factory method to deserialize from String value")
    }

    @Test
    fun `should return comments find by todo id`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))
        commentRepository.save(CommentEntity( null, "a comment", todo))
        commentRepository.save(CommentEntity(null, "a second comment", todo))

        //when
        val comments = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/comments").queryParam("todoId", todo.id).build() }
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Comment::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(comments!!.size).isEqualTo(2)
    }

    @Test
    fun `should return a bad request error when no todo id parameter given`() {
        webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/comments").build() }
            .exchange()
            .expectStatus().isBadRequest
            .expectBodyList(ApplicationError::class.java)
    }

    @Test
    fun `should return a comment when id given`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))
        val comment = commentRepository.save(CommentEntity( null, "a comment", todo))

        //when
        val createdComment = webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(comment.id) }
            .exchange()
            .expectStatus().isOk
            .expectBody(Comment::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(createdComment!!.id).isEqualTo(comment.id)
    }

    @Test
    fun `should return a not found error when id given`() {
        val noExistingId = 999L
        webTestClient.get()
            .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(noExistingId) }
            .exchange()
            .expectStatus().isNotFound
            .expectBody(ApplicationError::class.java)
    }

    @Test
    fun `should update a comment when found`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))
        val comment = commentRepository.save(CommentEntity( null, "a comment", todo))

        //when
        val updatedComment = Comment(comment.id!!, "changed description", todo.id!!)
        val changedComment = webTestClient.put()
            .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(comment.id) }
            .bodyValue(updatedComment)
            .exchange()
            .expectStatus().isOk
            .expectBody(Comment::class.java)
            .returnResult()
            .responseBody

        //then
        assertThat(changedComment!!.text).isEqualTo(updatedComment.text)
    }

    @Test
    fun `should delete a comment when found`() {
        //given
        val todo = todoRepository.save(TodoEntity(null, "todo", "this is a todo."))
        val comment = commentRepository.save(CommentEntity( null, "a comment", todo))

        //when
        webTestClient.delete()
            .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(comment.id) }
            .exchange()
            .expectStatus().isNoContent

        //then
        assertThat(commentRepository.existsById(comment.id!!)).isFalse
    }

}