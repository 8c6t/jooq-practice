package com.hachicore.demo.jooq.film;

import com.hachicore.demo.jooq.tables.daos.FilmDao;
import com.hachicore.demo.jooq.tables.pojos.Film;
import org.jooq.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class FilmRepositoryIsA extends FilmDao {

    public FilmRepositoryIsA(Configuration configuration) {
        super(configuration);
    }

    public Film findById(Long id) {
        return super.findById(id);
    }

}
