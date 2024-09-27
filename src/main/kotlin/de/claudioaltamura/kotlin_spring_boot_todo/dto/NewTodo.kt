package de.claudioaltamura.kotlin_spring_boot_todo.dto

import jakarta.validation.constraints.NotBlank

data class NewTodo(
    @get:NotBlank(message = "newToDo.title must not be blank")
    val title: String,
    @get:NotBlank(message = "newToDo.description must not be blank")
    val description: String
)