package com.ceos22.springcgv.domain;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class MovieLikeId implements Serializable {
    private Long user;
    private Long movie;
}