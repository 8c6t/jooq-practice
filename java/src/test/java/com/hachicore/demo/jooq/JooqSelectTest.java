package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.film.FilmRepository;
import com.hachicore.demo.jooq.film.FilmService;
import com.hachicore.demo.jooq.film.SimpleFilmInfo;
import com.hachicore.demo.jooq.tables.pojos.Film;
import com.hachicore.demo.jooq.web.response.FilmWithActorPagedResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqSelectTest {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private FilmService filmService;

    @Test
    @DisplayName("1) 영화정보 조회")
    void test() {
        Film film = filmRepository.findById(1L);
        assertThat(film).isNotNull();
    }

    @Test
    @DisplayName("2) 영화정보 간략 조회")
    void test2() {
        SimpleFilmInfo film = filmRepository.findByIdAsSimpleFilmInfo(1L);
        assertThat(film).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("3) 영화와 영화에 출연한 배우 정보를 페이징하여 조회")
    void test3() {
        FilmWithActorPagedResponse result = filmService.getFilmActorPageResponse(1L, 20L);
        assertThat(result.getFilmActor()).hasSize(20);
    }

}
