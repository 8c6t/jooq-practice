package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.tables.JActor;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JooqApplicationTests {

	@Autowired
	private DSLContext dslContext;

	@Test
	void test() {
		dslContext.selectFrom(JActor.ACTOR)
				.limit(10)
				.fetch();
	}

}
