package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TodoService {

    @Value("\${description}")
    lateinit var description : String

    fun addTodo(newTodo: NewTodo): Todo {
        //TODO persist
        return Todo(1L, newTodo.title, newTodo.description)
    }

    fun getTodos(page: Int, size: Int): List<Todo> {
        //TODO limit result
        return listOf(Todo(1, "first todo", description))
    }

    fun getTodo(id: Long): Todo {
        //TODO find by id
        return Todo(id, "first todo", description)
    }

}