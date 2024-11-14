package de.claudioaltamura.kotlin_spring_boot_todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class KotlinSpringBootTodoApplication

fun main(args: Array<String>) {
  runApplication<KotlinSpringBootTodoApplication>(*args)
}
