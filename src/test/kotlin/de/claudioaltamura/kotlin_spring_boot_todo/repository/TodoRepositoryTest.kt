package de.claudioaltamura.kotlin_spring_boot_todo.repository

import de.claudioaltamura.kotlin_spring_boot_todo.entity.TodoEntity
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class TodoRepositoryTest
@Autowired
constructor(val entityManager: TestEntityManager, val todoRepository: TodoRepository) {

  @BeforeEach
  fun setUp() {
    todoRepository.deleteAll()
  }

  @ParameterizedTest
  @MethodSource("todos")
  fun findByTitleContaining(name: String, expectedSize: Int) {
    val todo = TodoEntity(null, name, "this is a todo.")
    entityManager.persist(todo)

    val todoList = todoRepository.findByTitle(todo.title)

    assertThat(todoList.size).isEqualTo(expectedSize)
  }

  companion object {
    @JvmStatic
    fun todos(): Stream<Arguments> {
      return Stream.of(Arguments.arguments("todo", 1), Arguments.arguments("different", 1))
    }
  }
}
