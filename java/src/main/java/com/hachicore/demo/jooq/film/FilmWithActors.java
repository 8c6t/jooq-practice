package com.hachicore.demo.jooq.film;

import com.hachicore.demo.jooq.tables.pojos.Actor;
import com.hachicore.demo.jooq.tables.pojos.Film;
import com.hachicore.demo.jooq.tables.pojos.FilmActor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FilmWithActors {

    private final Film film;

    private final FilmActor filmActor;

    private final Actor actor;

    public Long getFilmId() {
        return film.getFilmId();
    }

    public String getFullActorName() {
        return actor.getFirstName() + " " + actor.getLastName();
    }

}