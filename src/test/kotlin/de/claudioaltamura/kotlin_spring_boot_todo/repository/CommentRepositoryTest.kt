package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.CommentEntity
import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class CommentRepositoryTest
@Autowired
constructor(
    val entityManager: TestEntityManager,
    val commentRepository: CommentRepository,
    val todoRepository: TodoRepository
) {

    @BeforeEach
    fun setUp() {
        commentRepository.deleteAll()
        todoRepository.deleteAll()
    }

    @Test
    fun findByTodo() {
        val todo = TodoEntity(null, "title", "this is a todo.")
        entityManager.persist(todo)
        val comment = CommentEntity(null, "comment", todo)
        entityManager.persist(comment)

        val comments = commentRepository.findByTodo(todo.id!!)

        assertThat(comments.size).isEqualTo(1)
    }
}
