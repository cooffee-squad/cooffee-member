import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.google.protobuf") version "0.9.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

apply(plugin = "com.google.protobuf")

group = "com.cooffee"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val grpcVersion = "1.59.0"
val protobufVersion = "3.21.7"
val protocVersion = protobufVersion
val snippetsDir by extra { file("build/generated-snippets") }
val asciidoctorExt: Configuration by configurations.creating

sourceSets {
    main {
        java {
            srcDir("src/main/generatedProto")
        }
    }
}

dependencies {
    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    // web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // postgresql
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    // liquibase
    implementation("org.liquibase:liquibase-core")

    // jwt
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.auth0:jwks-rsa:0.22.1")

    // gRPC
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")

    // kotest
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // restdocs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

    // testcontainer
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:$protocVersion" }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.builtins {
                all().forEach {
                    it.plugins {
                        id("grpc")
                    }
                }
            }
        }
        setGeneratedFilesBaseDir("$projectDir/src/generatedProto")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs += "-Xjsr305=strict"
                jvmTarget = "17"
            }
        }

        test {
            useJUnitPlatform()
            outputs.dir(snippetsDir)
        }

        asciidoctor {
            inputs.dir(snippetsDir)
            configurations("asciidoctorExt")
            dependsOn(test)

            doFirst {
                delete {
                    file("src/main/resources/static/docs")
                }
            }
        }

        register("copyHTML", Copy::class) {
            dependsOn(asciidoctor)
            from("build/docs/asciidoc")
            into(file("src/main/resources/static/docs"))
        }

        build {
            dependsOn(asciidoctor)
            dependsOn(getByName("copyHTML"))
        }

        bootJar {
            dependsOn(asciidoctor)
            dependsOn(getByName("copyHTML"))
        }
        jar {
            enabled = false
        }
    }
}
