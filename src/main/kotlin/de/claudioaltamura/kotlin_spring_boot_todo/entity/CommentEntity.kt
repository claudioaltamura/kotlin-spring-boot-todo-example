package de.claudioaltamura.kotlin_spring_boot_todo.entity

import jakarta.persistence.*

@Entity
@Table(name = "comments")
class CommentEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
    var text: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "todo_id",
        nullable = false,
        foreignKey = ForeignKey(name = "FK_TODOS_COMMENTS")
    )
    var todo: TodoEntity
) {
    override fun toString(): String {
        val todoId = todo.id
        return "Comment(id=$id, text='$text', todo='$todoId')"
    }
}
