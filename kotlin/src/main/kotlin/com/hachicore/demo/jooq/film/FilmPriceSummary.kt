package com.hachicore.demo.jooq.film

import java.math.BigDecimal

data class FilmPriceSummary(
    val filmId: Long,
    val title: String,
    val rentalRate: BigDecimal,
    val priceCategory: PriceCategory,
    val totalInventory: Long
)

enum class PriceCategory(
    val code: String
) {

    CHEAP("Cheap"),
    MODERATE("Moderate"),
    EXPENSIVE("Expensive");

    companion object {
        private val map = PriceCategory.values().associateBy(PriceCategory::code)
        fun findByCode(code: String): PriceCategory? = map[code]
    }

}