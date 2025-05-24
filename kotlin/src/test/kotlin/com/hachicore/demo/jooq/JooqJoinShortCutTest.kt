package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.film.FilmRepository
import com.hachicore.demo.jooq.film.FilmWithActor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqJoinShortCutTest(
    val filmRepository: FilmRepository
) {

    @Test
    @DisplayName("implicitPathJoin_테스트")
    fun implicitPathJoin_테스트() {
        val original: List<FilmWithActor> = filmRepository.findFilmWithActorList(1L, 10L)
        val implicit: List<FilmWithActor> = filmRepository.findFilmWithActorListImplicitPathJoin(1L, 10L)

        assertThat(original)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(implicit)
    }

    @Test
    @DisplayName("explicitPathJoin_테스트")
    fun explicitPathJoin_테스트() {
        val original: List<FilmWithActor> = filmRepository.findFilmWithActorList(1L, 10L)
        val implicit: List<FilmWithActor> = filmRepository.findFilmWithActorListExplicitPathJoin(1L, 10L)

        assertThat(original)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(implicit)
    }
}