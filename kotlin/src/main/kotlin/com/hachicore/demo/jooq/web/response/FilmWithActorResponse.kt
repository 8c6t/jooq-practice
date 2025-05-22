package com.hachicore.demo.jooq.web.response

import com.hachicore.demo.jooq.film.FilmWithActor

data class FilmWithActorResponse(
    val filmTitle: String?,
    val actorFullName: String?,
    val filmId: Long?
) {
    constructor(filmWithActor: FilmWithActor) : this(filmWithActor.title, filmWithActor.actorFullName, filmWithActor.filmId)
}

data class FilmWithActorPagedResponse(
    val page: PagedResponse,
    val filmActorList: List<FilmWithActorResponse>
) {
    companion object {
        fun of(page: PagedResponse, filmActorList: List<FilmWithActor>): FilmWithActorPagedResponse {
            return FilmWithActorPagedResponse(page, filmActorList.map { FilmWithActorResponse(it) })
        }
    }
}