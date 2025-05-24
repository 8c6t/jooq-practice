import org.jooq.meta.jaxb.*
import dev.monosoul.jooq.RecommendedVersions
import kotlinx.coroutines.withContext

val jooqVersion: String by extra("3.19.22")

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"

    id("dev.monosoul.jooq-docker") version "7.0.10"
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
    jooqCodegen("org.jooq:jooq:$jooqVersion")
    jooqCodegen("org.jooq:jooq-meta:$jooqVersion")
    jooqCodegen("org.jooq:jooq-codegen:$jooqVersion")

    jooqCodegen("org.flywaydb:flyway-core:${RecommendedVersions.FLYWAY_VERSION}")
    jooqCodegen("org.flywaydb:flyway-mysql:${RecommendedVersions.FLYWAY_VERSION}")
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
    withContainer {
        image {
            name = "mysql:8.0.29"
            envVars = mapOf(
                "MYSQL_ROOT_PASSWORD"   to "passwd",
                "MYSQL_DATABASE"        to "sakila"
            )
        }

        db {
            username = "root"
            password = "passwd"
            name = "sakila"
            port = 3306
            jdbc {
                schema = "jdbc:mysql"
                driverClassName = "com.mysql.cj.jdbc.Driver"
            }
        }
    }
}


tasks {
    generateJooqClasses {
        schemas.set(listOf("sakila"))
        basePackageName.set("com.hachicore.demo.jooq")
        outputDirectory.set(project.layout.projectDirectory.dir("build/generated-src/jooq/main"))
        includeFlywayTable.set(false)

        usingJavaConfig {
            generate = Generate()
                .withJavaTimeTypes(true)
                .withDeprecated(false)
                .withDaos(true)
                .withFluentSetters(true)
                .withRecords(true)

            withStrategy(
                Strategy().withName("com.hachicore.demo.jooq.generator.JPrefixGeneratorStrategy")
            )

            database.withForcedTypes(
                ForcedType()
                    .withUserType("java.lang.Long")
                    .withTypes("int unsigned"),
               ForcedType()
                    .withUserType("java.lang.Integer")
                    .withTypes("tinyint unsigned"),
                ForcedType()
                    .withUserType("java.lang.Integer")
                    .withTypes("smallint unsigned")
            )
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
