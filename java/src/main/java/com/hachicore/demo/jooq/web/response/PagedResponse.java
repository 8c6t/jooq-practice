package com.hachicore.demo.jooq.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse {
    private long page;
    private long pageSize;
}