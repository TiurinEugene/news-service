import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "test.sample.news"
version = "1.0.0"

plugins {
    val kotlinVersion = "1.2.50"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.springframework.boot") version "2.0.3.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile("org.springframework.boot", "spring-boot-starter-actuator")
    compile("org.springframework.boot", "spring-boot-starter-web")
    compile("org.springframework.boot", "spring-boot-starter-websocket")
    compile("org.springframework.boot", "spring-boot-starter-data-jpa")
    compile("org.springframework.boot", "spring-boot-starter-data-redis")
    compile("org.springframework.boot", "spring-boot-starter-security")
    compile("org.springframework.session", "spring-session-data-redis")
    compile("com.fasterxml.jackson.module", "jackson-module-kotlin")

    runtime("org.springframework.boot", "spring-boot-devtools")
    runtime("org.postgresql", "postgresql")

    testCompile("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
