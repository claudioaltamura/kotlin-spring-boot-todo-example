package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.dto.Comment
import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewComment
import de.claudioaltamura.kotlin_spring_boot_todo.service.CommentService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/comments")
class CommentController(val commentService: CommentService) {

    @PostMapping
    fun addComment(@RequestBody @Valid newComment: NewComment): ResponseEntity<Comment> {
        logger.info { "add comment: '${newComment}'" }
        val comment = commentService.addComment(newComment)
        val location : URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.id).toUri()
        return ResponseEntity.created(location).body(comment)
    }

    @GetMapping("/{id}")
    fun getComment(@PathVariable("id") id: Long) : ResponseEntity<Comment> {
        logger.info { "get comment for '${id}'" }
        return ResponseEntity.ok(commentService.getComment(id))
    }

    @GetMapping
    fun getComments(@RequestParam("todoId") todoId: Long) : ResponseEntity<List<Comment>> {
        logger.info { "get comments by todoId: '${todoId}'" }
        return ResponseEntity.ok(commentService.getCommentsByTodo(todoId))
    }

    @PutMapping("/{id}")
    fun updateComment(@RequestBody @Valid comment: Comment, @PathVariable("id") @Min(value = 0)  id : Long)  : ResponseEntity<Comment> {
        logger.info { "update comment '${id}', '${comment}'" }
        return ResponseEntity.ok(commentService.updateComment(id, comment))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(@PathVariable("id") @Min(value = 0) id : Long){
        logger.info { "delete comment '${id}'" }
        commentService.deleteComment(id)
    }

}