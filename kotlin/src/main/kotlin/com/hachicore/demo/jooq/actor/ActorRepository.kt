package com.hachicore.demo.jooq.actor

import com.hachicore.demo.jooq.tables.JActor
import com.hachicore.demo.jooq.tables.JFilm
import com.hachicore.demo.jooq.tables.JFilmActor
import com.hachicore.demo.jooq.tables.daos.ActorDao
import com.hachicore.demo.jooq.tables.pojos.Actor
import com.hachicore.demo.jooq.tables.pojos.Film
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
}
