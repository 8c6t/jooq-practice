package com.hachicore.demo.jooq.film

import com.hachicore.demo.jooq.tables.JActor
import com.hachicore.demo.jooq.tables.JFilm
import com.hachicore.demo.jooq.tables.JFilmActor
import com.hachicore.demo.jooq.tables.daos.FilmDao
import com.hachicore.demo.jooq.tables.pojos.Film
import org.jooq.Configuration
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class FilmRepository(
    private val dslContext: DSLContext
) {

    private val FILM: JFilm = JFilm.FILM

    fun findById(id: Long): Film? {
        return dslContext
            .selectFrom(FILM)
            .where(FILM.FILM_ID.eq(id))
            .fetchOneInto(Film::class.java)
    }

    fun findSimpleFilmInfoById(id: Long): SimpleFilmInfo? {
        return dslContext
            .select(
                FILM.FILM_ID,
                FILM.TITLE,
                FILM.DESCRIPTION
            )
            .from(FILM)
            .where(FILM.FILM_ID.eq(id))
            .fetchOneInto(SimpleFilmInfo::class.java)
    }

    fun findFilmWithActorList(page: Long, pageSize: Long): List<FilmWithActor> {
        val FILM_ACTOR = JFilmActor.FILM_ACTOR
        val ACTOR = JActor.ACTOR

        return dslContext
            .select(
                FILM,
                FILM_ACTOR,
                ACTOR
            )
            .from(FILM)
            .join(FILM_ACTOR)
            .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
            .join(ACTOR)
            .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .fetchInto(FilmWithActor::class.java)
    }

}

@Repository
class FilmRepositoryIsA(
    val configuration: Configuration
): FilmDao(configuration) {

    override fun findById(id: Long): Film? {
        return super.findById(id)
    }

}

@Repository
class FilmRepositoryHasA(
    val configuration: Configuration,
    val dao: FilmDao = FilmDao(configuration)
) {

    fun findById(id: Long): Film? {
        return dao.fetchOneByJFilmId(id)
    }

    fun findByRangeBetween(from: Int, to: Int): List<Film> {
        return dao.fetchRangeOfJLength(from, to)
    }

}