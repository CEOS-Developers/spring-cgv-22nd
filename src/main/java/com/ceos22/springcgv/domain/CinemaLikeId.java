package com.ceos22.springcgv.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class CinemaLikeId implements Serializable {

    private Long user;

    private Long cinema;

}