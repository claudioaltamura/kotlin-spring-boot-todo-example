package de.claudioaltamura.kotlin_spring_boot_todo.exception

class CommentNotFoundException(override val message: String) : RuntimeException(message)
