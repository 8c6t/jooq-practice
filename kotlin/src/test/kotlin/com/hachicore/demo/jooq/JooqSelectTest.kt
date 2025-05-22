package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.film.FilmRepository
import com.hachicore.demo.jooq.film.FilmService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqSelectTest(
    val filmRepository: FilmRepository,
    val filmService: FilmService
) {

    @Test
    @DisplayName("1) 영화정보 조회")
    fun test() {
        val film = filmRepository.findById(1)
        assertThat(film).isNotNull()
    }

    @Test
    @DisplayName("2) 영화정보 간략 조회")
    fun test2() {
        val film = filmRepository.findSimpleFilmInfoById(1)
        assertThat(film).hasNoNullFieldsOrProperties()
    }

    @Test
    @DisplayName("3) 영화와 영화에 출연한 배우 정보를 페이징하여 조회")
    fun test3() {
        val result = filmService.getFilmActorPageResponse(1L, 20L)
        assertThat(result.filmActorList).hasSize(20)
    }

}