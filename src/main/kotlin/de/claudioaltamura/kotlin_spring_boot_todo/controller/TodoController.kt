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
    fun addTodo(@RequestBody @Valid newTodo: NewTodo): ResponseEntity<Todo> {
        logger.info { "add todo: '${newTodo}'" }
        val todo = todoService.addTodo(newTodo)
        val location : URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(todo.id).toUri()
        return ResponseEntity.created(location).body(todo)
    }

    @GetMapping
    fun getTodos(@RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") size: Int) : ResponseEntity<List<Todo>> {
        logger.info { "get todos: '${page}', '${size}'" }
        return ResponseEntity.ok(todoService.getTodos(page, size))
    }

    @GetMapping("/{id}")
    fun getTodo(@PathVariable("id") id: Long) : ResponseEntity<Todo> {
        logger.info { "get todo for '${id}'" }
        return ResponseEntity.ok(todoService.getTodo(id))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateTodo(@RequestBody todo: Todo,
                     @PathVariable("id") id : Int) {
        logger.info { "update todo '${id}', '${todo}'" }
        todoService.updateTodo(id, todo)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable("id") id : Int){
        logger.info { "delete todo '${id}'" }
        todoService.deleteTodo(id)
    }

}