package com.hachicore.demo.jooq.config.converter;

import com.hachicore.demo.jooq.film.FilmPriceSummary;
import org.jooq.impl.EnumConverter;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory> {

    public PriceCategoryConverter() {
        super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
    }

}
