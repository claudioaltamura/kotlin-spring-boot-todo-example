package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface TodoRepository : CrudRepository<TodoEntity, Long> {

  @Query(value = "SELECT t FROM TodoEntity t WHERE t.title like %:title%")
  fun findByTitle(@Param("title") title: String): List<TodoEntity>
}
