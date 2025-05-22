package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.film.FilmRepositoryHasA;
import com.hachicore.demo.jooq.film.FilmRepositoryIsA;
import com.hachicore.demo.jooq.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqDaoWrapperTest {

    @Autowired
    FilmRepositoryIsA filmRepositoryIsA;

    @Autowired
    FilmRepositoryHasA filmRepositoryHasA;

    @Test
    @DisplayName("" +
            "상속) 자동생성 DAO 사용" +
            "영화 길이가 100 ~ 180분 사이인 영화 조회" +
            "")
    void 상속_DAO_1() {
        int start = 100;
        int end = 180;

        List<Film> films = filmRepositoryIsA.fetchRangeOfJLength(start, end);

        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }

    @Test
    @DisplayName("" +
            "컴포지션) 자동생성 DAO 사용" +
            "영화 길이가 100 ~ 180분 사이인 영화 조회" +
            "")
    void 컴포지션_DAO_1() {
        int start = 100;
        int end = 180;

        List<Film> films = filmRepositoryHasA.findByRangeBetween(start, end);

        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }

}
