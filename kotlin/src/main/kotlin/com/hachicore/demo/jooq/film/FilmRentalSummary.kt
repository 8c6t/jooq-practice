package com.hachicore.demo.jooq.film

import java.math.BigDecimal

data class FilmRentalSummary(
    val filmId: Long,
    val title: String,
    val averageRentalDuration: BigDecimal
)