package com.hachicore.demo.jooq.film

import com.hachicore.demo.jooq.config.converter.PriceCategoryConverter
import com.hachicore.demo.jooq.tables.*
import com.hachicore.demo.jooq.tables.daos.FilmDao
import com.hachicore.demo.jooq.tables.pojos.Film
import com.hachicore.demo.jooq.tables.references.FILM
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.DatePart
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.springframework.stereotype.Repository
import java.math.BigDecimal

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

    fun findFilmWithActorListImplicitPathJoin(page: Long, pageSize: Long): List<FilmWithActor> {
        val FILM_ACTOR = JFilmActor.FILM_ACTOR
        return dslContext
            .select(
                FILM,
                FILM_ACTOR,
                FILM_ACTOR.actor
            )
            .from(FILM)
            .join(FILM_ACTOR)
            .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .fetchInto(FilmWithActor::class.java)
    }

    fun findFilmWithActorListExplicitPathJoin(page: Long, pageSize: Long): List<FilmWithActor> {
        return dslContext
            .select(
                FILM,
                FILM.filmActor,
                FILM.filmActor.actor
            )
            .from(FILM)
            .join(FILM.filmActor())
            .join(FILM.filmActor().actor())
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
    val dslContext: DSLContext,
    val dao: FilmDao = FilmDao(configuration)
) {

    fun findById(id: Long): Film? {
        return dao.fetchOneByJFilmId(id)
    }

    fun findByRangeBetween(from: Int, to: Int): List<Film> {
        return dao.fetchRangeOfJLength(from, to)
    }

    fun findFilmPriceSummaryByFilmTitleLike(filmTitle: String): List<FilmPriceSummary> {
        val INVENTORY = JInventory.INVENTORY
        return dslContext
            .select(
                FILM.FILM_ID,
                FILM.TITLE,
                FILM.RENTAL_RATE,
                case_()
                    .`when`(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                    .`when`(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                    .else_("Expensive")
                    .`as`("price_category").convert(PriceCategoryConverter()),
                selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField<Int>("total_inventory")
            ).from(FILM)
            .where(FILM.TITLE.like("%$filmTitle%"))
            .fetchInto(FilmPriceSummary::class.java)
    }

    fun findFilmRentalSummaryByFilmTitleLike(filmTitle: String) : List<FilmRentalSummary> {
        val INVENTORY = JInventory.INVENTORY
        val RENTAL = JRental.RENTAL

        val rentalDurationInfoSubquery = select(
            INVENTORY.FILM_ID,
            avg(localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).`as`("average_rental_duration")
        ).from(RENTAL)
            .join(INVENTORY)
            .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
            .where(RENTAL.RENTAL_DATE.isNotNull())
            .groupBy(INVENTORY.FILM_ID)
            .asTable("rental_duration_info")

        return dslContext
            .select(
                FILM.FILM_ID,
                FILM.TITLE,
                rentalDurationInfoSubquery.field("average_rental_duration")
            )
            .from(FILM)
            .join(rentalDurationInfoSubquery)
            .on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(INVENTORY.FILM_ID)))
            .where(FILM.TITLE.like("%$filmTitle%"))
            .orderBy(DSL.field(DSL.name("average_rental_duration")).desc())
            .fetchInto(FilmRentalSummary::class.java)
    }

    fun findRentedFilmByTitle(filmTitle: String): List<Film> {
        val INVENTORY = JInventory.INVENTORY
        val RENTAL = JRental.RENTAL

        val subquery = selectOne()
            .from(INVENTORY)
            .join(RENTAL)
            .on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
            .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))

        return dslContext
            .selectFrom(FILM)
            .whereExists(subquery)
                .and(FILM.TITLE.like("%$filmTitle%"))
            .fetchInto(Film::class.java)
    }

}