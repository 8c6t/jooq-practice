package com.hachicore.demo.jooq.film;

import com.hachicore.demo.jooq.tables.daos.FilmDao;
import com.hachicore.demo.jooq.tables.pojos.Film;
import org.jooq.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilmRepositoryHasA {

    private final FilmDao dao;

    public FilmRepositoryHasA(Configuration configuration) {
        this.dao = new FilmDao(configuration);
    }

    public Film findById(Long id) {
        return dao.fetchOneByJFilmId(id);
    }

    public List<Film> findByRangeBetween(Integer from, Integer to) {
        return dao.fetchRangeOfJLength(from, to);
    }

}
