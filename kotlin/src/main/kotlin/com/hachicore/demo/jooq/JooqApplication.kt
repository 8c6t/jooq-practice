package com.hachicore.demo.jooq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JooqApplication

fun main(args: Array<String>) {
	runApplication<JooqApplication>(*args)
}
