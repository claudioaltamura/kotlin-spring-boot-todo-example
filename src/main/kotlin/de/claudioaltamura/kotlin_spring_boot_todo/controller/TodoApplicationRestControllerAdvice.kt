package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.dto.ApplicationError
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class TodoApplicationRestControllerAdvice {

    @ExceptionHandler(Exception::class)
    fun handleTodoNotFoundException(exception: Exception): ApplicationError = ApplicationError(
        HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message!!
    )

    @ExceptionHandler(TodoNotFoundException::class)
    fun handleTodoNotFoundException(exception: TodoNotFoundException): ApplicationError = ApplicationError(
        HttpStatus.NOT_FOUND.value(), exception.message
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleTodoNotFoundException(exception: MethodArgumentNotValidException): ApplicationError {
        val errors = exception.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()
            .joinToString {
                it
            }

        return ApplicationError(HttpStatus.BAD_REQUEST.value(), errors)
    }

}