plugins {
	kotlin("jvm") version "2.0.21"
	kotlin("plugin.spring") version "2.0.21"
	kotlin("plugin.jpa") version "2.0.21"
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.github.ben-manes.versions") version "0.51.0"
	id("com.diffplug.spotless") version "6.23.2"
}

spotless {
	kotlin {
		ktfmt().googleStyle()
	}
}

group = "de.claudioaltamura"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql:42.7.4")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("io.mockk:mockk:1.13.13")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
