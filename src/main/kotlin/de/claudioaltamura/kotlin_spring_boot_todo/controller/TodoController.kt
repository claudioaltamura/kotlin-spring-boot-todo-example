package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.service.TodoService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todos")
class TodoController(val todoService: TodoService) {

    @GetMapping("/{id}")
    fun todo(@Valid @PathVariable("id") id: Long) : Todo {
        return todoService.getTodo(id)
    }
}