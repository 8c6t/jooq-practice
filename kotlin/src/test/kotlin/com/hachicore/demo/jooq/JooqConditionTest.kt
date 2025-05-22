package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.actor.ActorFilmographySearchOption
import com.hachicore.demo.jooq.actor.ActorRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqConditionTest(
    val actorRepository: ActorRepository
) {

    @Test
    @DisplayName("and 조건 검색 - fistName와 LastName이 일치하는 배우 조회")
    fun AND조건_1() {
        val firstName = "ED"
        val lastName = "CHASE"

        val actorList = actorRepository.findByFirstnameAndLastName(firstName, lastName)
        assertThat(actorList).hasSize(1)
    }

    @Test
    @DisplayName("or 조건 검색 - fistName 또는 LastName이 일치하는 배우 조회")
    fun or조건_1() {
        val firstName = "ED"
        val lastName = "CHASE"

        val actorList = actorRepository.findByFirstnameOrLastName(firstName, lastName)
        assertThat(actorList).hasSizeGreaterThan(1)
    }

    @Test
    @DisplayName("in절 - 동적 조건 검색")
    fun in절_동적_조건검색_1() {
        val actorList = actorRepository.findByActorIdIn(listOf(1L))
        assertThat(actorList).hasSize(1)
    }

    @Test
    @DisplayName("in절 - 동적 조건 검색 - empty list시 제외")
    fun in절_동적_조건검색_2() {
        val actorList = actorRepository.findByActorIdIn(emptyList())
        assertThat(actorList).hasSizeGreaterThan(1)
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름으로 조회")
    fun 다중_조건_검색_1() {
        var searchOption = ActorFilmographySearchOption(actorName = "LOLLOBRIGIDA")

        val actorFilmographies = actorRepository.findActorFilmography(searchOption)
        assertThat(actorFilmographies).hasSize(1)
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름과 영화 제목으로 조회")
    fun 다중_조건_검색_2() {
        val searchOption = ActorFilmographySearchOption("LOLLOBRIGIDA", "COMMANDMENTS EXPRESS")

        val actorFilmographies = actorRepository.findActorFilmography(searchOption)

        assertThat(actorFilmographies).hasSize(1);
        assertThat(actorFilmographies.get(0).filmList).hasSize(1);
    }

}