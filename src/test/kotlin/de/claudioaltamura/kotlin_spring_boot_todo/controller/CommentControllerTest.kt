package de.claudioaltamura.kotlin_spring_boot_todo.controller

import com.ninjasquad.springmockk.MockkBean
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Comment
import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewComment
import de.claudioaltamura.kotlin_spring_boot_todo.service.CommentService
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

@WebMvcTest(CommentController::class)
@AutoConfigureWebTestClient
class CommentControllerTest {

    @Autowired lateinit var webTestClient: WebTestClient

    @MockkBean lateinit var commentService: CommentService

    @Test
    fun `should add a comment successfully`() {
        val newComment = NewComment("this is a comment.", 1L)

        every { commentService.addComment(any()) }.returns(Comment(1L, "this is a comment.", 1L))

        val createdComment =
            webTestClient
                .post()
                .uri("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newComment)
                .exchange()
                .expectStatus()
                .isCreated
                .expectBody(Comment::class.java)
                .returnResult()
                .responseBody

        assertThat(createdComment!!.id).isEqualTo(1L)

        verify { commentService.addComment(any()) }
    }

    @Test
    fun `should return a comment when id given`() {
        every { commentService.getComment(any()) }.returns(Comment(1L, "a comment", 1L))

        val comment =
            webTestClient
                .get()
                .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(1L) }
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Comment::class.java)
                .returnResult()
                .responseBody

        assertThat(comment!!.id).isEqualTo(1L)

        verify { commentService.getComment(any()) }
    }

    @Test
    fun `should return comments by todo id`() {
        every { commentService.getCommentsByTodo(any()) }
            .returns(listOf(Comment(1L, "a todo", 1L), Comment(2L, "a second todo", 1L)))

        val comments =
            webTestClient
                .get()
                .uri { uriBuilder -> uriBuilder.path("/comments").queryParam("todoId", 1L).build() }
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList(Comment::class.java)
                .returnResult()
                .responseBody

        assertThat(comments!!.size).isEqualTo(2)
    }

    @Test
    fun `should update a comment when found`() {
        val updatedComment = Comment(1L, "changed comment", 1L)

        every { commentService.updateComment(any(), any()) }.returns(updatedComment)

        val comment =
            webTestClient
                .put()
                .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(1L) }
                .bodyValue(updatedComment)
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(Comment::class.java)
                .returnResult()
                .responseBody

        assertThat(comment!!.text).isEqualTo("changed comment")

        verify { commentService.updateComment(any(), any()) }
    }

    @Test
    fun `should delete a comment when found`() {
        justRun { commentService.deleteComment(any()) }

        webTestClient
            .delete()
            .uri { uriBuilder -> uriBuilder.path("/comments/{id}").build(1L) }
            .exchange()
            .expectStatus()
            .isNoContent

        verify(exactly = 1) { commentService.deleteComment(any()) }
    }
}
