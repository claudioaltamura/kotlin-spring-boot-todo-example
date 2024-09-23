![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/claudioaltamura/kotlin-spring-boot-todo-example/ci.yml?branch=main)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# kotlin-spring-boot-todo-example
Example project with Kotlin and Spring Boot

## Example

GET
```
    curl -i http://localhost:8080/todos/1
```

POST
```
    curl -i \
    -d '{"title":"a todo", "description": "details about the todo"}' \
    -H "Content-Type: application/json" \
    -X POST http://localhost:8080/todos
```