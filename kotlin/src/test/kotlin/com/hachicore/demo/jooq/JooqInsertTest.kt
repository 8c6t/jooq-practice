package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.actor.ActorRepository
import com.hachicore.demo.jooq.tables.pojos.Actor
import com.hachicore.demo.jooq.tables.records.ActorRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.List

@SpringBootTest
class JooqInsertTest(
    val actorRepository: ActorRepository
) {

    @Test
    @DisplayName("자동생성된 DAO를 통한 insert")
    @Transactional
    fun insert_dao() {
        // given
        var actor = Actor(
            firstName = "John",
            lastName = "Doe",
            lastUpdate = LocalDateTime.now()
        );

        // when
        actorRepository.saveByDao(actor);

        // then
        assertThat(actor.actorId).isNotNull();
    }


    @Test
    @DisplayName("ActiveRecord 를 통한 insert")
    @Transactional
    fun insert_by_record() {
        // given
        val actor = Actor(
            firstName = "John",
            lastName = "Doe",
            lastUpdate = LocalDateTime.now()
        )

        // when
        val newActorRecord: ActorRecord = actorRepository.saveByRecord(actor)

        // then
        assertThat(actor.actorId).isNull()
        assertThat(newActorRecord.actorId).isNotNull()
    }

    @Test
    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    fun insert_with_returning_pk() {
        // given
        val actor = Actor(
            firstName = "John",
            lastName = "Doe",
            lastUpdate = LocalDateTime.now()
        )

        // when
        val pk: Long? = actorRepository.saveWithReturningPkOnly(actor)

        // then
        assertThat(pk).isNotNull()
    }

    @Test
    @DisplayName("SQL 실행 후 해당 ROW 전체 반환")
    @Transactional
    fun insert_with_returning() {
        // given
        val actor = Actor(
            firstName = "John",
            lastName = "Doe",
            lastUpdate = LocalDateTime.now()
        )

        // when
        val pk: Actor? = actorRepository.saveWithReturning(actor)

        // then
        assertThat(pk).hasNoNullFieldsOrProperties()
    }

    /**
     * insert into `actor` (`first_name`, `last_name`) values (?, ?), (?, ?), (?, ?), (?, ?), (?, ?)
     */
    @Test
    @DisplayName("bulk insert 예제")
    @Transactional
    fun bulk_insert() {
        // given
        val actor1 = Actor(
            firstName = "John",
            lastName = "Doe",
            lastUpdate = LocalDateTime.now()
        )

        val actor2 = Actor(
            firstName = "John 2",
            lastName = "Doe 2",
            lastUpdate = LocalDateTime.now()
        )

        val actorList = listOf(actor1, actor2)

        // when
        actorRepository.bulkInsertWithRows(actorList)
    }
}