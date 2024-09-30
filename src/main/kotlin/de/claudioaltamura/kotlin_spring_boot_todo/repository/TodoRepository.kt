package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface TodoRepository : CrudRepository<TodoEntity, Int> {

    @Query(value = "SELECT * FROM TODOS WHERE title like %?1%", nativeQuery = true)
    fun findByTitle(title : String) : List<TodoEntity>

}