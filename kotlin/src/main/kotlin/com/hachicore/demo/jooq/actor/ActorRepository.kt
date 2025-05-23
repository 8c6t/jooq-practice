package com.hachicore.demo.jooq.actor

import com.hachicore.demo.jooq.tables.JActor
import com.hachicore.demo.jooq.tables.JFilm
import com.hachicore.demo.jooq.tables.JFilmActor
import com.hachicore.demo.jooq.tables.daos.ActorDao
import com.hachicore.demo.jooq.tables.pojos.Actor
import com.hachicore.demo.jooq.tables.pojos.Film
import com.hachicore.demo.jooq.tables.records.ActorRecord
import com.hachicore.demo.jooq.utils.JooqListConditionUtil.Companion.inIfNotEmpty
import org.jooq.Condition
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class ActorRepository(
    private val dslContext: DSLContext,
    private val configuration: Configuration
) {

    private val actorDao = ActorDao(configuration)
    private val ACTOR = JActor.ACTOR

    fun findByFirstnameAndLastName(firstName: String, lastName: String): List<Actor> {
        return dslContext
            .selectFrom(ACTOR)
            .where(
                ACTOR.FIRST_NAME.eq(firstName),
                ACTOR.LAST_NAME.eq(lastName)
            )
            .fetchInto(Actor::class.java)
    }

    fun findByFirstnameOrLastName(firstName: String, lastName: String): List<Actor> {
        return dslContext
            .selectFrom(ACTOR)
            .where(
                ACTOR.FIRST_NAME.eq(firstName).or(ACTOR.LAST_NAME.eq(lastName))
            )
            .fetchInto(Actor::class.java)
    }

    fun findByActorIdIn(ids: List<Long>?): List<Actor> {
        return dslContext
            .selectFrom(ACTOR)
            .where(inIfNotEmpty(ACTOR.ACTOR_ID, ids))
            .fetchInto(Actor::class.java)
    }

    fun findActorFilmography(searchOption: ActorFilmographySearchOption): List<ActorFilmography> {
        val FILM_ACTOR = JFilmActor.FILM_ACTOR
        val FILM = JFilm.FILM

        val actorListMap = dslContext
            .select(ACTOR, FILM)
            .from(ACTOR)
            .join(FILM_ACTOR)
                .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
            .join(FILM)
                .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
            .where(
                containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), searchOption.actorName),
                containsIfNotBlank(FILM.TITLE, searchOption.filmTitle)
            )
            .fetchGroups(
                { record -> record[ACTOR.name, Actor::class.java] },
                { record -> record[FILM.name, Film::class.java] }
            )

        return actorListMap.entries
            .map { entry -> ActorFilmography(entry.key, entry.value) }
    }

    private fun containsIfNotBlank(field: Field<String?>, inputValue: String?): Condition {
        if (inputValue.isNullOrBlank()) {
            return DSL.noCondition()
        }
        return field.likeRegex(inputValue)
    }

    fun saveByDao(actor: Actor): Actor {
        actorDao.insert(actor)
        return actor
    }

    fun saveByRecord(actor: Actor): ActorRecord {
        val actorRecord = dslContext.newRecord(ACTOR, actor)
        actorRecord.insert()
        return actorRecord
    }

    fun saveWithReturningPkOnly(actor: Actor): Long? {
        return dslContext
            .insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            ).values(
                actor.firstName,
                actor.lastName
            ).returningResult(ACTOR.ACTOR_ID)
            .fetchOneInto(Long::class.java)
    }

    fun saveWithReturning(actor: Actor): Actor? {
        return dslContext
            .insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            ).values(
                actor.firstName,
                actor.lastName
            ).returning(*ACTOR.fields())
            .fetchOneInto(Actor::class.java)
    }

    fun bulkInsertWithRows(actorList: List<Actor>) {
        val rows = actorList.map { DSL.row(it.firstName, it.lastName) }
        dslContext
            .insertInto(
                ACTOR,
                ACTOR.FIRST_NAME,
                ACTOR.LAST_NAME
            )
            .valuesOfRows(rows)
            .execute()
    }

    fun update(actor: Actor) {
        actorDao.update(actor)
    }

    fun findByActorId(actorId: Long): Actor? {
        return actorDao.findById(actorId)
    }

    fun updateWithDto(actorId: Long, request: ActorUpdateRequest): Int {
        val firstName = if (!request.firstName.isNullOrBlank()) DSL.`val`(request.firstName) else DSL.noField(ACTOR.FIRST_NAME)
        val lastName = if (!request.lastName.isNullOrBlank()) DSL.`val`(request.lastName) else DSL.noField(ACTOR.LAST_NAME)

        return dslContext.update(ACTOR)
            .set(ACTOR.FIRST_NAME, firstName)
            .set(ACTOR.LAST_NAME, lastName)
            .where(ACTOR.ACTOR_ID.eq(actorId))
            .execute()
    }

    fun updateWithRecord(actorId: Long, request: ActorUpdateRequest): Int {
        val actorRecord = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId)) ?: throw RuntimeException()

        if (!request.firstName.isNullOrBlank()) {
            actorRecord.firstName = request.firstName
        }

        if (!request.lastName.isNullOrBlank()) {
            actorRecord.lastName = request.lastName
        }

        return actorRecord.update()
    }

    fun delete(actorId: Long): Int {
        return dslContext.deleteFrom(ACTOR)
            .where(ACTOR.ACTOR_ID.eq(actorId))
            .execute()
    }

    fun deleteWithActiveRecord(actorId: Long): Int {
        val actorRecord = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId)) ?: throw RuntimeException()
        return actorRecord.delete()
    }

    fun findRecordByActorId(actorId: Long): ActorRecord {
        return dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(actorId)) ?: throw RuntimeException()
    }

}
