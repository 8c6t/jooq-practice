import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

val jooqVersion: String by extra("3.19.22")

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"

    id("org.jooq.jooq-codegen-gradle") version "3.19.22"
}

group = "com.hachicore.demo"
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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    jooqCodegen(project(":jooq-custom"))
    jooqCodegen("com.mysql:mysql-connector-j")
    jooqCodegen("org.jooq:jooq")
    jooqCodegen("org.jooq:jooq-meta")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

var dbUser     = System.getProperty("db-user")    ?: "root"
var dbPasswd   = System.getProperty("db-passwd")  ?: "passwd"

jooq {
    executions {
        create("sakilaDB") {
            configuration {
                logging = Logging.WARN
                jdbc {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306"
                    user = dbUser
                    password = dbPasswd
                }

                generator {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        isUnsignedTypes = true
                        inputSchema = "sakila"
                        forcedTypes = listOf(
                            ForcedType().apply {
                                userType = "java.lang.Long"
                                includeTypes = "int unsigned"
                            },
                            ForcedType().apply {
                                userType = "java.lang.Integer"
                                includeTypes = "tinyint unsigned"
                            },
                            ForcedType().apply {
                                userType = "java.lang.Integer"
                                includeTypes = "smallint unsigned"
                            }
                        )
                    }

                    generate {
                        isDaos = true
                        isRecords = true
                        isFluentSetters = true
                        isJavaTimeTypes = true
                        isDeprecated = false
                    }

                    target {
                        packageName = "com.hachicore.demo.jooq"
                        directory = "build/generated-src/jooq/main"
                    }

                    strategy {
                        name = "com.hachicore.demo.jooq.generator.JPrefixGeneratorStrategy"
                    }
                }
            }
        }
    }
}

tasks.named("compileKotlin") {
    dependsOn(tasks.named("jooqCodegen"))
}

sourceSets {
    main {
        kotlin {
            srcDirs(listOf("src/main/kotlin", "build/generated-src/jooq/main"))
        }
    }
}
