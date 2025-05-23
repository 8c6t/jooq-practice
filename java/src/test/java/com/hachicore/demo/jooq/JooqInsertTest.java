package com.hachicore.demo.jooq;

import com.hachicore.demo.jooq.actor.ActorRepository;
import com.hachicore.demo.jooq.tables.pojos.Actor;
import com.hachicore.demo.jooq.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqInsertTest {

    @Autowired
    ActorRepository actorRepository;

    @Test
    @DisplayName("자동생성된 DAO를 통한 insert")
    @Transactional
    void insert_dao() {
        // given
        var actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        actorRepository.saveByDao(actor);

        // then
        assertThat(actor.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("ActiveRecord 를 통한 insert")
    @Transactional
    void insert_by_record() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        ActorRecord newActorRecord = actorRepository.saveByRecord(actor);

        // then
        assertThat(actor.getActorId()).isNull();
        assertThat(newActorRecord.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    void insert_with_returning_pk() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Long pk = actorRepository.saveWithReturningPkOnly(actor);

        // then
        assertThat(pk).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 해당 ROW 전체 반환")
    @Transactional
    void insert_with_returning() {
        // given
        var actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Actor pk = actorRepository.saveWithReturning(actor);

        // then
        assertThat(pk).hasNoNullFieldsOrProperties();
    }

    /**
     * insert into `actor` (`first_name`, `last_name`) values (?, ?), (?, ?), (?, ?), (?, ?), (?, ?)
     */
    @Test
    @DisplayName("bulk insert 예제")
    @Transactional
    void bulk_insert() {
        // given
        Actor actor1 = new Actor();
        actor1.setFirstName("John");
        actor1.setLastName("Doe");
        actor1.setLastUpdate(LocalDateTime.now());

        Actor actor2 = new Actor();
        actor2.setFirstName("John 2");
        actor2.setLastName("Doe 2");
        actor2.setLastUpdate(LocalDateTime.now());

        List<Actor> actorList = List.of(actor1, actor2);

        // when
        actorRepository.bulkInsertWithRows(actorList);
    }

}
