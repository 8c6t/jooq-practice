package com.hachicore.demo.jooq.actor

import com.hachicore.demo.jooq.tables.pojos.Actor
import com.hachicore.demo.jooq.tables.pojos.Film

data class ActorFilmography(
    val actor: Actor,
    val filmList: List<Film>
)