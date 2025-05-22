package com.hachicore.demo.jooq.web.response

data class PagedResponse(
    val page: Long = 0,
    val pageSize: Long = 0
)