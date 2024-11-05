package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.dto.Comment
import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewComment
import de.claudioaltamura.kotlin_spring_boot_todo.entity.CommentEntity
import de.claudioaltamura.kotlin_spring_boot_todo.exception.CommentNotFoundException
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import de.claudioaltamura.kotlin_spring_boot_todo.repository.CommentRepository
import de.claudioaltamura.kotlin_spring_boot_todo.repository.TodoRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentService(val commentRepository: CommentRepository, val todoRepository: TodoRepository) {

    fun addComment(newComment: NewComment): Comment {
        val todo = todoRepository.findById(newComment.todoId)
        if (!todo.isPresent) {
            throw TodoNotFoundException("no todo found for the id '$todo.id'.")
        }
        val comment = newComment.let {
            CommentEntity(null, it.text, todo.get())
        }

        commentRepository.save(comment)

        return comment.let {
            Comment(it.id!!, it.text, it.todo.id!!)
        }
    }

    fun getCommentsByTodo(todoId: Long): List<Comment> {
        val comments = commentRepository.findByTodo(todoId)

        return comments.map {
            Comment(it.id!!, it.text, it.todo.id!!)
        }
    }

    fun getComment(id: Long): Comment {
        val existingComment = commentRepository.findById(id)

        return if (existingComment.isPresent) {
            existingComment.get()
                .let {
                    Comment(it.id!!, it.text, it.todo.id!!)
                }
        } else {
            throw CommentNotFoundException("no comment found for the id '$id'.")
        }
    }

    fun updateComment(id: Long, comment: Comment): Comment {
        val existingComment = commentRepository.findById(id)

        return if (existingComment.isPresent) {
            existingComment.get()
                .let {
                    it.text = comment.text
                    it.todo.id = comment.todoId
                    commentRepository.save(it)
                    Comment(it.id!!, it.text, it.todo.id!!)
                }
        } else {
            throw CommentNotFoundException("no comment found for the id '$id'.")
        }
    }

    fun deleteComment(id: Long) {
        val existingComment = commentRepository.findById(id)
        if (existingComment.isPresent) {
            existingComment.get()
                .let {
                    commentRepository.deleteById(id)
                }
        } else {
            throw CommentNotFoundException("no comment found for the id '$id'.")
        }
    }

}