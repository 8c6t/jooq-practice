package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.tables.JActor
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqApplicationTests(
	val dslContext: DSLContext
) {

	@Test
	fun test() {
		dslContext.selectFrom(JActor.ACTOR)
			.limit(10)
			.fetch()
	}

}
