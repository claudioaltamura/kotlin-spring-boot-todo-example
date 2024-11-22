package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.AbstractDatabaseIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest : AbstractDatabaseIntegrationTest() {

  @Autowired lateinit var commentRepository: CommentRepository

  @Test
  @Sql("/comment_find_by_id.sql")
  fun findByTodo() {
    val todoId = 100L
    val comments = commentRepository.findByTodo(todoId)

    assertThat(comments.size).isEqualTo(1)
  }
}
