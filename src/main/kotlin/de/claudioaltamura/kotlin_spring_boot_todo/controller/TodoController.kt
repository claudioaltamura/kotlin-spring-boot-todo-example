package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.service.TodoService
import jakarta.validation.Valid
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/todos")
class TodoController(val todoService: TodoService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTodo(@RequestBody @Valid newTodo: NewTodo): ResponseEntity<Todo> {
        logger.info { "add todo: '${newTodo}'" }
        val todo = todoService.addTodo(newTodo)
        val location : URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(todo.id).toUri()
        return ResponseEntity.created(location).body(todo)
    }

    @GetMapping("/{id}")
    fun getTodo(@Valid @PathVariable("id") id: Long) : ResponseEntity<Todo> {
        logger.info { "get todo for '${id}'" }
        return ResponseEntity.ok(todoService.getTodo(id))
    }
}