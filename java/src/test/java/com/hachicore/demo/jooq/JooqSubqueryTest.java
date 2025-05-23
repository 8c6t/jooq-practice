package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.film.FilmPriceSummary;
import com.hachicore.demo.jooq.film.FilmRentalSummary;
import com.hachicore.demo.jooq.film.FilmRepositoryHasA;
import com.hachicore.demo.jooq.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqSubqueryTest {

    @Autowired
    private FilmRepositoryHasA filmRepository;

    @Test
    @DisplayName("""
            영화별 대여료가
             1.0 이하면 'Cheap',
             3.0 이하면 'Moderate',
             그 이상이면 'Expensive'로 분류하고,
            각 영화의 총 재고 수를 조회한다.
            """)
    void 스칼라_서브쿼리_예제() {
        List<FilmPriceSummary> filmPriceSummaryList = filmRepository.findFilmPriceSummaryByFilmTitleLike("EGG");
        assertThat(filmPriceSummaryList).isNotEmpty();
    }

    @Test
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    void from절_서브쿼리_인라인뷰_예제() {
        List<FilmRentalSummary> filmRentalSummaryList = filmRepository.findFilmRentalSummaryByFilmTitleLike("EGG");
        assertThat(filmRentalSummaryList).isNotEmpty();
    }

    @Test
    @DisplayName("대여된 기록이 있는 영화가 있는 영화만 조회")
    void 조건절_서브쿼리_예제() {
        List<Film> filmList = filmRepository.findRentedFilmByTitle("EGG");
        assertThat(filmList).isNotEmpty();
    }

}
