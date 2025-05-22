package com.hachicore.demo.jooq.utils;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class JooqListConditionUtil {

    public static <T> Condition inIfNotEmpty(Field<T> idField, List<T> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return DSL.noCondition();
        }
        return idField.in(ids);
    }

}
