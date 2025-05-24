package com.hachicore.demo.jooq.config

import org.jooq.ExecuteContext
import org.jooq.ExecuteListener
import org.jooq.tools.StopWatch
import org.slf4j.LoggerFactory
import java.time.Duration

class PerformanceListener: ExecuteListener {
    private val log = LoggerFactory.getLogger(this.javaClass)

    private lateinit var stopWatch: StopWatch
    private val SLOW_QUERY_LIMIT: Duration = Duration.ofSeconds(3)

    override fun executeStart(ctx: ExecuteContext?) {
        stopWatch = StopWatch()
    }

    override fun executeEnd(ctx: ExecuteContext?) {
        val queryTimeNano = stopWatch.split()

        if (queryTimeNano > SLOW_QUERY_LIMIT.nano) {
            val query = ctx?.query()
            val executeTime = Duration.ofNanos(queryTimeNano)
            log.warn("""
                    ### Slow SQL 탐지 >>
                    경고: jOOQ로 실행된 쿼리 중 {}초 이상 실행된 쿼리가 있습니다.
                    실행시간: {}초
                    실행쿼리: {}
                    """.trimIndent(),
                SLOW_QUERY_LIMIT.toSeconds(),
                millisToSeconds(executeTime),
                query
            )
        }
    }

    private fun millisToSeconds(duration: Duration): String {
        return String.format("%.1f", duration.toMillis() / 1000.0)
    }

}