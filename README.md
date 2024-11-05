![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/claudioaltamura/kotlin-spring-boot-todo-example/ci.yml?branch=main)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# kotlin-spring-boot-todo-example
Example project with Kotlin and Spring Boot

## Example

Todos

GET
```
    curl -i http://localhost:8080/todos/1
```

GET
```
    curl -i 'http://localhost:8080/todos?title=Title'
```

POST
```
    curl -i \
    -d '{"title":"a todo", "description": "details about the todo"}' \
    -H "Content-Type: application/json" \
    -X POST http://localhost:8080/todos
```

PUT
```
    curl -i \
    -d '{"id": 1, "title":"a todo", "description": "but a better description"}' \
    -H "Content-Type: application/json" \
    -X PUT http://localhost:8080/todos/1
```

DELETE
```
    curl -i \
    -H "Content-Type: application/json" \
    -X DELETE http://localhost:8080/todos/1
```

Comments

GET
```
    curl -i http://localhost:8080/comments/1
```

GET
```
    curl -i 'http://localhost:8080/comments?todoId=1'
```


POST
```
    curl -i \
    -d '{"text":"a comment", "todoId": 1}' \
    -H "Content-Type: application/json" \
    -X POST http://localhost:8080/comments
```

PUT
```
    curl -i \
    -d '{"id": 1, "text":"updated note", "todoId": 1}' \
    -H "Content-Type: application/json" \
    -X PUT http://localhost:8080/comments/1
```

DELETE
```
    curl -i \
    -H "Content-Type: application/json" \
    -X DELETE http://localhost:8080/comments/1
```


## H2

```
    http://localhost:8080/h2-console
```