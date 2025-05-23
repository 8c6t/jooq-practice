package com.hachicore.demo.jooq

import com.hachicore.demo.jooq.actor.ActorRepository
import com.hachicore.demo.jooq.actor.ActorUpdateRequest
import com.hachicore.demo.jooq.tables.pojos.Actor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class JooqUpdateTest(
    val actorRepository: ActorRepository
) {

    @Test
    @Transactional
    @DisplayName("pojo를 사용한 update")
    fun 업데이트_with_pojo() {
        // given
        val newActor = Actor(firstName = "Tom", lastName = "Cruise")
        val actor = actorRepository.saveWithReturning(newActor)

        // when
        actor!!.firstName = "Suri"
        actorRepository.update(actor)

        // then
        val updatedActor = actorRepository.findByActorId(actor.actorId!!)
        assertThat(updatedActor!!.firstName).isEqualTo("Suri")
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - DTO 활용")
    fun 업데이트_일부_필드만() {
        // given
        val newActor = Actor(firstName = "Tom", lastName = "Cruise")

        val newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        val request = ActorUpdateRequest(firstName = "Suri")

        // when
        actorRepository.updateWithDto(newActorId!!, request);

        // then
        val updatedActor = actorRepository.findByActorId(newActorId!!);
        assertThat(updatedActor!!.firstName).isEqualTo("Suri");
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - record 활용")
    fun 업데이트_일부_필드만_with_record() {
        // given
        val newActor = Actor(firstName = "Tom", lastName = "Cruise")

        val newActorId = actorRepository.saveWithReturningPkOnly(newActor)
        val request = ActorUpdateRequest(firstName = "Suri")

        // when
        actorRepository.updateWithRecord(newActorId!!, request)

        // then
        val updatedActor = actorRepository.findByActorId(newActorId!!)
        assertThat(updatedActor!!.firstName).isEqualTo("Suri")
    }

    @Test
    @Transactional
    @DisplayName("delete 예제")
    fun delete_예제() {
        // given
        val newActor = Actor(firstName = "Tom", lastName = "Cruise")
        val newActorId = actorRepository.saveWithReturningPkOnly(newActor)

        // when
        val delete = actorRepository.delete(newActorId!!)

        // then
        assertThat(delete).isEqualTo(1)
    }

    @Test
    @Transactional
    @DisplayName("delete 예제 - with active record")
    fun delete_with_active_record_예제() {
        // given
        val newActor = Actor(firstName = "Tom", lastName = "Cruise")
        val newActorId = actorRepository.saveWithReturningPkOnly(newActor)

        // when
        val delete = actorRepository.deleteWithActiveRecord(newActorId!!)

        // then
        assertThat(delete).isEqualTo(1)
    }

}