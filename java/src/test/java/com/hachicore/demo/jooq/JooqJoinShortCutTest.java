package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.film.FilmRepository;
import com.hachicore.demo.jooq.film.FilmWithActors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqJoinShortCutTest {

    @Autowired
    FilmRepository filmRepository;

    @Test
    @DisplayName("implicitPathJoin_테스트")
    void implicitPathJoin_테스트() {

        List<FilmWithActors> original = filmRepository.findFilmWithActorsList(1L, 10L);
        List<FilmWithActors> implicit = filmRepository.findFilmWithActorsListImplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

    @Test
    @DisplayName("explicitPathJoin_테스트")
    void explicitPathJoin_테스트() {

        List<FilmWithActors> original = filmRepository.findFilmWithActorsList(1L, 10L);
        List<FilmWithActors> implicit = filmRepository.findFilmWithActorsListExplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }
}