package de.claudioaltamura.kotlin_spring_boot_todo.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class Todo(
    @get:NotNull(message = "todo.id must not be null")
    val id: Int,
    @get:NotBlank(message = "todo.title must not be blank")
    val title: String,
    @get:NotBlank(message = "todo.description must not be blank")
    val description: String
)
