package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.actor.ActorRepository
import com.hachicore.demo.jooq.tables.JActor
import com.hachicore.demo.jooq.tables.records.ActorRecord
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class JooqActiveRecordTest(
    val actorRepository: ActorRepository,
    val dslContext: DSLContext
) {

    @Test
    @DisplayName("SELECT 절 예제")
    fun activeRecord_조회_예제() {
        // given
        val actorId = 1L

        // when
        val actor: ActorRecord = actorRepository.findRecordByActorId(actorId)

        // then
        assertThat(actor).hasNoNullFieldsOrProperties()
    }

    @Test
    @DisplayName("activeRecord refresh 예제")
    fun activeRecord_refresh_예제() {
        // given
        val actorId = 1L
        val actor: ActorRecord = actorRepository.findRecordByActorId(actorId)
        actor.firstName = null

        // when
        actor.refresh()

        // then
        assertThat(actor.firstName).isNotBlank()
    }

    @Test
    @DisplayName("activeRecord store 예제 - insert")
    @Transactional
    fun activeRecord_insert_예제() {
        // given
        val actorRecord = dslContext.newRecord(JActor.ACTOR)

        // when
        actorRecord.firstName = "john"
        actorRecord.lastName = "doe"
        actorRecord.store()

        // 혹은
        // actor.insert();

        // then
        assertThat(actorRecord.lastUpdate).isNull()
    }

    @Test
    @DisplayName("activeRecord store 예제 - update")
    @Transactional
    fun activeRecord_update_예제() {
        // given
        val actorId = 1L
        val newName = "test"
        val actor: ActorRecord = actorRepository.findRecordByActorId(actorId)

        // when
        actor.firstName = newName
        actor.store()

        // 혹은
        // actor.update();

        // then
        assertThat(actor.firstName).isEqualTo(newName)
    }

    @Test
    @DisplayName("activeRecord delete 예제")
    @Transactional
    fun activeRecord_delete_예제() {
        // given
        val actorRecord = dslContext.newRecord(JActor.ACTOR)

        // when
        actorRecord.firstName = "john"
        actorRecord.lastName = "doe"
        actorRecord.store()

        // when
        actorRecord.delete()

        // then
        assertThat(actorRecord).hasNoNullFieldsOrProperties()
    }

}