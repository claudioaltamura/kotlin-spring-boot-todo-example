package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.CommentEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface CommentRepository : CrudRepository<CommentEntity, Long> {

    @Query(value = "SELECT c FROM CommentEntity c WHERE c.todo.id = :todoId")
    fun findByTodo(@Param("todoId") todoId : Long) : List<CommentEntity>

}