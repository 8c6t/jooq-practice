package com.hachicore.demo.jooq.actor;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ActorFilmographySearchOption {

    private final String actorName;
    private final String filmTitle;

}
