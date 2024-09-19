package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.controller.Todo
import org.springframework.stereotype.Service

@Service
class TodoService {

    fun getTodo(id: Long): Todo {
        return Todo(id, "first todo", "this is the first todo")
    }

}