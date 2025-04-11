plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("plugin.serialization") version "2.1.20"
    id("ch.acanda.gradle.fabrikt") version "1.14.0"
}

group = "net.moviepumpkins"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

configurations {
    all { exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.29")
    implementation("io.swagger.core.v3:swagger-models:2.2.29")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly(group = "org.springframework.boot", name = "spring-boot-starter-log4j2")
    runtimeOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.get()}/build/generated/sources/fabrikt")
        }
    }
}

fabrikt {
    generate("core-api") {
        apiFile = file("src/main/resources/core.api.yaml")
        basePackage = "net.moviepumpkins.core.integration"
        externalReferenceResolution = targeted
        outputDirectory = file("build/generated/sources/fabrikt")
        sourcesPath = "src/main/kotlin"
        resourcesPath = "src/main/resources"
        validationLibrary = Jakarta
        typeOverrides {
            datetime = OffsetDateTime
            date = LocalDate
        }
        controller {
            generate = enabled
            target = Spring
        }
        model {
            generate = enabled
            extensibleEnums = disabled
            serializationLibrary = Jackson
            sealedInterfacesForOneOf = enabled
        }
    }
}

tasks {

    withType<Test> {
        useJUnitPlatform()
    }
}