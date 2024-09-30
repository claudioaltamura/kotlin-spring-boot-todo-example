package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import de.claudioaltamura.kotlin_spring_boot_todo.repository.TodoRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class TodoService(val todoRepository: TodoRepository) {

    fun addTodo(newTodo: NewTodo): Todo {
        val todoEntity = newTodo.let {
            TodoEntity(null, it.title, it.description)
        }
        todoRepository.save(todoEntity)
        logger.info { "saved todo is : $todoRepository" }
        return todoEntity.let {
            Todo(it.id!!, it.title, it.description)
        }
    }

    fun getTodos(title: String?): List<Todo> {
        val todos = title?.let {
            todoRepository.findByTitle(title)
        } ?: todoRepository.findAll()

        return todos.map {
            it.id?.let { it1 -> Todo(it1, it.title, it.description) }!!
        }
    }

    fun getTodo(id: Int): Todo {
        //TODO find by id
        return Todo(id, "first todo", "description")
    }

    fun updateTodo(id: Int, todo: Todo) {
        //TODO update
    }

    fun deleteTodo(id: Int) {
        //TODO update
    }

}