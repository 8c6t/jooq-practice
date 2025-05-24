package com.hachicore.demo.jooq

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.dual
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqSlowQueryTest(
    val dslContext: DSLContext
) {

    @Test
    @DisplayName("SLOW 쿼리 탐지 테스트")
    fun 슬로우쿼리_탐지_테스트() {
        dslContext.select(DSL.field("SLEEP(4)"))
            .from(dual())
            .execute()
    }

}