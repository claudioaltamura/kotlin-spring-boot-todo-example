package de.claudioaltamura.kotlin_spring_boot_todo.controller

import de.claudioaltamura.kotlin_spring_boot_todo.dto.ApplicationError
import de.claudioaltamura.kotlin_spring_boot_todo.exception.TodoNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class TodoApplicationRestControllerAdvice {

    @ExceptionHandler(TodoNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleTodoNotFoundException(exception: TodoNotFoundException): ApplicationError = ApplicationError(
        HttpStatus.NOT_FOUND.value(), exception.message
    )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestValueException(exception: HttpMessageNotReadableException): ApplicationError = ApplicationError(
        HttpStatus.BAD_REQUEST.value(), exception.message!!
    )

    @ExceptionHandler(NoResourceFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestValueException(exception: NoResourceFoundException): ApplicationError = ApplicationError(
        HttpStatus.BAD_REQUEST.value(), exception.message!!
    )

    @ExceptionHandler(MissingRequestValueException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestValueException(exception: MissingRequestValueException): ApplicationError = ApplicationError(
        HttpStatus.BAD_REQUEST.value(), exception.message!!
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ApplicationError {
        val errors = exception.bindingResult.fieldErrors
            .map { fieldError -> fieldError.defaultMessage!! }
            .joinToString(", ") {
                it
            }

        return ApplicationError(HttpStatus.BAD_REQUEST.value(), errors)
    }

    @ExceptionHandler(Exception::class)
    fun handleTodoNotFoundException(exception: Exception): ApplicationError = ApplicationError(
        HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message!!
    )

}