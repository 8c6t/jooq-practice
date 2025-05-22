plugins {
    kotlin("jvm")
}

val jooqVersion: String by extra("3.19.22")

group = "com.hachicore.demo"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jooq:jooq-codegen:$jooqVersion")
    runtimeOnly("com.mysql:mysql-connector-j:9.1.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
