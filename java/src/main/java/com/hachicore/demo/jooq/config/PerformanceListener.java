package com.hachicore.demo.jooq.config;

import lombok.extern.slf4j.Slf4j;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Query;
import org.jooq.tools.StopWatch;

import java.time.Duration;

@Slf4j
public class PerformanceListener implements ExecuteListener {

    private StopWatch watch;

    private static final Duration SLOW_QUERY_LIMIT = Duration.ofSeconds(3);

    @Override
    public void executeStart(ExecuteContext ctx) {
        watch = new StopWatch();
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        long queryTimeNano = watch.split();
        if (queryTimeNano > SLOW_QUERY_LIMIT.getNano()) {
            Query query = ctx.query();
            Duration executeTime = Duration.ofNanos(queryTimeNano);
            log.warn("""
                    ### Slow SQL 탐지 >>
                    경고: jOOQ로 실행된 쿼리 중 {}초 이상 실행된 쿼리가 있습니다.
                    실행시간: {}초
                    실행쿼리: {}
                    """, SLOW_QUERY_LIMIT.toSeconds(), millisToSeconds(executeTime), query);
        }
    }

    private String millisToSeconds(Duration duration) {
        return String.format("%.1f", duration.toMillis() / 1000.0);
    }

}
