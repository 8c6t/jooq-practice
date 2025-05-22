package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.film.FilmRepositoryHasA
import com.hachicore.demo.jooq.film.FilmRepositoryIsA
import com.hachicore.demo.jooq.tables.pojos.Film
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqDaoWrapperTest(
    val filmRepositoryIsA: FilmRepositoryIsA,
    val filmRepositoryHasA: FilmRepositoryHasA
) {

    @Test
    @DisplayName(
        ("" +
                "상속) 자동생성 DAO 사용" +
                "영화 길이가 100 ~ 180분 사이인 영화 조회" +
                "")
    )
    fun 상속_DAO_1() {
        val start = 100
        val end = 180

        val films = filmRepositoryIsA.fetchRangeOfJLength(start, end)

        assertThat(films).allSatisfy { film: Film ->
            assertThat(film.length).isBetween(start, end)
        }
    }

    @Test
    @DisplayName(
        ("" +
                "컴포지션) 자동생성 DAO 사용" +
                "영화 길이가 100 ~ 180분 사이인 영화 조회" +
                "")
    )
    fun 컴포지션_DAO_1() {
        val start = 100
        val end = 180

        val films = filmRepositoryHasA.findByRangeBetween(start, end)

        assertThat(films).allSatisfy { film: Film ->
            assertThat(film.length).isBetween(start, end)
        }
    }

}