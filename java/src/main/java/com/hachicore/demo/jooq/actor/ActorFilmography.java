package com.hachicore.demo.jooq.actor;

import com.hachicore.demo.jooq.tables.pojos.Actor;
import com.hachicore.demo.jooq.tables.pojos.Film;
import lombok.Getter;

import java.util.List;

@Getter
public class ActorFilmography {

    private final Actor actor;
    private final List<Film> filmList;

    public ActorFilmography(Actor actor, List<Film> filmList) {
        this.actor = actor;
        this.filmList = filmList;
    }

}
