package de.claudioaltamura.kotlin_spring_boot_todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class Comment(
  @get:NotNull(message = "comment.id must not be null") val id: Long,
  @get:NotBlank(message = "comment.text must not be blank") val text: String,
  @get:NotNull(message = "comment.todoId must not be null") val todoId: Long
)
