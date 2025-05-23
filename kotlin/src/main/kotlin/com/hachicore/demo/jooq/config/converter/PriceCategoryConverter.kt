package com.hachicore.demo.jooq.config.converter

import com.hachicore.demo.jooq.film.PriceCategory
import org.jooq.impl.EnumConverter

class PriceCategoryConverter: EnumConverter<String, PriceCategory>(
    String::class.java, PriceCategory::class.java, PriceCategory::code
)