package com.hachicore.demo.jooq.utils

import org.jooq.Condition
import org.jooq.Field
import org.jooq.impl.DSL

class JooqListConditionUtil {

    companion object {
        fun <T> inIfNotEmpty(idField: Field<T>, ids: List<T>?): Condition {
            return if (ids.isNullOrEmpty()) DSL.noCondition() else idField.`in`(ids)
        }
    }

}