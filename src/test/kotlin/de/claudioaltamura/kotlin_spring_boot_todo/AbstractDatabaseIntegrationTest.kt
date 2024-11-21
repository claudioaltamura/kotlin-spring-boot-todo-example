package de.claudioaltamura.kotlin_spring_boot_todo

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@ContextConfiguration(initializers = [AbstractDatabaseIntegrationTest.Initializer::class])
abstract class AbstractDatabaseIntegrationTest {
  companion object {
    @Container
    val container =
      PostgreSQLContainer<Nothing>("postgres:latest").apply { withDatabaseName("tododb") }
  }

  internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
      container.start()

      TestPropertyValues.of(
          "spring.datasource.url=${container.jdbcUrl}",
          "spring.datasource.username=${container.username}",
          "spring.datasource.password=${container.password}",
          "spring.flyway.url=${container.jdbcUrl}",
          "spring.flyway.user=${container.username}",
          "spring.flyway.password=${container.password}"
        )
        .applyTo(configurableApplicationContext.environment)
    }
  }
}
