package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.CommentEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<CommentEntity, Long> {

    @Query(value = "SELECT * FROM COMMENTS WHERE TODO_ID = ?1", nativeQuery = true)
    fun findByTodo(todoId : Long) : List<CommentEntity>

}