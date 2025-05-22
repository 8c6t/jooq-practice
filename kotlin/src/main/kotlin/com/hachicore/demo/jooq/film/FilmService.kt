package com.hachicore.demo.jooq.film

import com.hachicore.demo.jooq.web.response.FilmWithActorPagedResponse
import com.hachicore.demo.jooq.web.response.PagedResponse
import org.springframework.stereotype.Service

@Service
class FilmService(
    val filmRepository: FilmRepository
) {

    fun getFilmActorPageResponse(page: Long, pageSize: Long): FilmWithActorPagedResponse {
        return FilmWithActorPagedResponse.of(
            PagedResponse(page, pageSize),
            filmRepository.findFilmWithActorList(page, pageSize)
        )
    }

}