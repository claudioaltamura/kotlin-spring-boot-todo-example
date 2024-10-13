package de.claudioaltamura.kotlin_spring_boot_todo.service

import de.claudioaltamura.kotlin_spring_boot_todo.dto.NewTodo
import de.claudioaltamura.kotlin_spring_boot_todo.dto.Todo
import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import de.claudioaltamura.kotlin_spring_boot_todo.repository.TodoRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
@Transactional
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
            Todo(it.id!!, it.title, it.description)
        }
    }

    fun getTodo(id: Int): Todo {
        val existingTodo = todoRepository.findById(id)

        return if (existingTodo.isPresent) {
            existingTodo.get()
                .let {
                    Todo(it.id!!, it.title, it.description)
                }
        } else {
            throw TodoNotFoundException("no todo found for the id '$id'.")
        }
    }

    fun updateTodo(id: Int, todo: Todo): Todo {
        val existingTodo = todoRepository.findById(id)

        return if (existingTodo.isPresent) {
            existingTodo.get()
                .let {
                    it.title = todo.title
                    it.description = todo.description
                    todoRepository.save(it)
                    Todo(it.id!!, it.title, it.description)
                }
        } else {
            throw TodoNotFoundException("no todo found for the id '$id'.")
        }
    }

    fun deleteTodo(id: Int) {
        val existingTodo = todoRepository.findById(id)
        if (existingTodo.isPresent) {
            existingTodo.get()
                .let {
                    todoRepository.deleteById(id)
                }
        } else {
            throw TodoNotFoundException("no todo found for the id '$id'.")
        }
    }

}