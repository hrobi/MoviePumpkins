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
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.29")
    implementation("io.swagger.core.v3:swagger-models:2.2.29")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-database-postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly(group = "org.springframework.boot", name = "spring-boot-starter-log4j2")
    runtimeOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.0")
    testCompileOnly("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.6.0")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:10.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
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

val serverApiSpecFile = "src/main/resources/server-api.yml"

fabrikt {
    generate("core-api-server") {
        apiFile = file(serverApiSpecFile)
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

    register<Copy>("copyOpenApiSpec") {
        group = "openapi tools"
        from(serverApiSpecFile)
        into("../frontend/moviepumpkins-web")
        rename(".*", ".core-apispec.yml")
        filter { line ->
            line.replace(
                "#[CLIENT_ONLY] ",
                ""
            )
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}