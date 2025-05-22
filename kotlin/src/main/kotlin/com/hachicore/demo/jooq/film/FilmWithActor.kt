package com.hachicore.demo.jooq.film

import com.hachicore.demo.jooq.tables.pojos.Actor
import com.hachicore.demo.jooq.tables.pojos.Film
import com.hachicore.demo.jooq.tables.pojos.FilmActor

data class FilmWithActor(
    val film: Film,
    val filmActor: FilmActor,
    val actor: Actor,
) {

    val filmId: Long?
        get() = this.film.filmId
    val title: String?
        get() = this.film.title
    val actorFullName: String?
        get() = "${this.actor.firstName} ${this.actor.lastName}"

}