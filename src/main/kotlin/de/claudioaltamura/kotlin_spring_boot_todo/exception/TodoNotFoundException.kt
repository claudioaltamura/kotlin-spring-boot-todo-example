package de.claudioaltamura.kotlin_spring_boot_todo.exception

class TodoNotFoundException(override val message: String) : RuntimeException(message)
