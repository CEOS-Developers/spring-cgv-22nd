package com.ceos22.springcgv.domain.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CinemaLikeId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long user;
    private Long cinema;
}