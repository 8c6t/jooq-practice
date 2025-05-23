package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.film.FilmRepositoryHasA
import com.hachicore.demo.jooq.tables.pojos.Film
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqSubqueryTest(
    val filmRepository: FilmRepositoryHasA
) {

    @Test
    @DisplayName("""
            영화별 대여료가
             1.0 이하면 'Cheap',
             3.0 이하면 'Moderate',
             그 이상이면 'Expensive'로 분류하고,
            각 영화의 총 재고 수를 조회한다.
            """)
    fun 스칼라_서브쿼리_예제() {
        val filmPriceSummaries = filmRepository.findFilmPriceSummaryByFilmTitleLike("EGG")
        assertThat(filmPriceSummaries).isNotEmpty()
    }

    @Test
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    fun from절_서브쿼리_인라인뷰_예제() {
        val filmRentalSummaryList = filmRepository.findFilmRentalSummaryByFilmTitleLike("EGG")
        assertThat(filmRentalSummaryList).isNotEmpty()
    }

    @Test
    @DisplayName("대여된 기록이 있는 영화가 있는 영화만 조회")
    fun 조건절_서브쿼리_예제() {
        val filmList: List<Film> = filmRepository.findRentedFilmByTitle("EGG")
        assertThat(filmList).isNotEmpty()
    }

}