package de.claudioaltamura.kotlin_spring_boot_todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class NewComment(
    @get:NotBlank(message = "newComment.text must not be blank") val text: String,
    @get:NotNull(message = "newComment.todoId must not be null") val todoId: Long
)
