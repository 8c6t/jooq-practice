package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.actor.ActorRepository;
import com.hachicore.demo.jooq.actor.ActorUpdateRequest;
import com.hachicore.demo.jooq.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqUpdateTest {

    @Autowired
    ActorRepository actorRepository;

    @Test
    @Transactional
    @DisplayName("pojo를 사용한 update")
    void 업데이트_with_pojo() {
        // given
        var newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Actor actor = actorRepository.saveWithReturning(newActor);

        // when
        actor.setFirstName("Suri");
        actorRepository.update(actor);

        // then
        Actor updatedActor = actorRepository.findByActorId(actor.getActorId());
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - DTO 활용")
    void 업데이트_일부_필드만() {
        // given
        var newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        var request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithDto(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName())
                .isEqualTo("Suri");
    }

    @Test
    @Transactional
    @DisplayName("일부 필드만 update - record 활용")
    void 업데이트_일부_필드만_with_record() {
        // given
        var newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        var request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithRecord(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @Test
    @Transactional
    @DisplayName("delete 예제")
    void delete_예제() {

        // given
        var newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        int delete = actorRepository.delete(newActorId);

        // then
        assertThat(delete)
                .isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("delete 예제 - with active record")
    void delete_with_active_record_예제() {

        // given
        var newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        int delete = actorRepository.deleteWithActiveRecord(newActorId);

        // then
        assertThat(delete).isEqualTo(1);
    }

}