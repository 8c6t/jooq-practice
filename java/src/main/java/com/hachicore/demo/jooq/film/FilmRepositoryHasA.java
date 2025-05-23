package com.hachicore.demo.jooq.film;

import com.hachicore.demo.jooq.config.converter.PriceCategoryConverter;
import com.hachicore.demo.jooq.tables.JFilm;
import com.hachicore.demo.jooq.tables.JInventory;
import com.hachicore.demo.jooq.tables.JRental;
import com.hachicore.demo.jooq.tables.daos.FilmDao;
import com.hachicore.demo.jooq.tables.pojos.Film;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static org.jooq.impl.DSL.*;

@Repository
public class FilmRepositoryHasA {

    private final DSLContext dslContext;
    private final FilmDao dao;
    private final JFilm FILM = JFilm.FILM;

    public FilmRepositoryHasA(DSLContext dslContext, Configuration configuration) {
        this.dslContext = dslContext;
        this.dao = new FilmDao(configuration);
    }

    public Film findById(Long id) {
        return dao.fetchOneByJFilmId(id);
    }

    public List<Film> findByRangeBetween(Integer from, Integer to) {
        return dao.fetchRangeOfJLength(from, to);
    }

    public List<FilmPriceSummary> findFilmPriceSummaryByFilmTitleLike(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        return dslContext
                .select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.RENTAL_RATE,
                        case_()
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                                .else_("Expensive")
                                .as("price_category").convert(new PriceCategoryConverter()),
                        selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField("total_inventory")
                )
                .from(FILM)
                .where(FILM.TITLE.like("%" + filmTitle + "%"))
                .fetchInto(FilmPriceSummary.class);
    }

    public List<FilmRentalSummary> findFilmRentalSummaryByFilmTitleLike(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        final JRental RENTAL = JRental.RENTAL;

        var rentalDurationInfoSubquery = select(
                INVENTORY.FILM_ID,
                avg(localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).as("average_rental_duration")
        ).from(RENTAL)
        .join(INVENTORY)
        .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
        .where(RENTAL.RENTAL_DATE.isNotNull())
        .groupBy(INVENTORY.FILM_ID)
        .asTable("rental_duration_info");

        return dslContext
                .select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        rentalDurationInfoSubquery.field("average_rental_duration")
                )
                .from(FILM)
                .join(rentalDurationInfoSubquery)
                .on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(INVENTORY.FILM_ID)))
                .where(FILM.TITLE.like("%" + filmTitle + "%"))
                .orderBy(field(name("average_rental_duration")).desc())
                .fetchInto(FilmRentalSummary.class);
    }

    public List<Film> findRentedFilmByTitle(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        final JRental RENTAL = JRental.RENTAL;

        var subquery = selectOne()
                .from(INVENTORY)
                .join(RENTAL)
                .on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
                .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID));

        return dslContext
                .selectFrom(FILM)
                .whereExists(subquery)
                    .and(FILM.TITLE.like("%" + filmTitle + "%"))
                .fetchInto(Film.class);
    }
}
