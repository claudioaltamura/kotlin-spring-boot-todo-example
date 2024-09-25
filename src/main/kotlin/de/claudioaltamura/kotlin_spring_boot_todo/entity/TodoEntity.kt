package de.claudioaltamura.kotlin_spring_boot_todo.entity

import jakarta.persistence.*

@Entity
@Table(name="todos")
data class TodoEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int?,
    var title: String,
    var description: String
){

    override fun toString(): String {
        return "Todo(id=$id, title='$title', description='$description'"
    }
}