package de.claudioaltamura.kotlin_spring_boot_todo.entity

import jakarta.persistence.*

@Entity
@Table(name = "todos")
class TodoEntity(
  @Id
  @SequenceGenerator(name = "todos_seq", sequenceName = "todos_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todos_seq")
  var id: Long?,
  var title: String,
  var description: String
) {
  override fun toString(): String {
    return "Todo(id=$id, title='$title', description='$description')"
  }
}
