package com.hachicore.demo.jooq.film;

import com.hachicore.demo.jooq.web.response.FilmWithActorPagedResponse;
import com.hachicore.demo.jooq.web.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmWithActorPagedResponse getFilmActorPageResponse(Long page, Long pageSize) {
        return new FilmWithActorPagedResponse(
                new PagedResponse(page, pageSize),
                filmRepository.findFilmWithActorsList(page, pageSize)
        );
    }

}
