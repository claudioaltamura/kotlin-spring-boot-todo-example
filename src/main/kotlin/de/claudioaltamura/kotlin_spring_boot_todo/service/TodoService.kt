package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.controller.Todo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TodoService {

    @Value("\${description}")
    lateinit var description : String

    fun getTodo(id: Long): Todo {
        return Todo(id, "first todo", description)
    }

}