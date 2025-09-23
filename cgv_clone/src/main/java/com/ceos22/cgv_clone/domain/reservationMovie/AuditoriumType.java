package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "auditorium_type")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditoriumType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditorium_type_id")
    private Long id;

    @Column(nullable=false, length=50, unique = true)
    private String code; // REGULAR, IMAX, 4DX ...

    @Column(nullable=false, length=100)
    private String name;

    @OneToOne(mappedBy = "auditoriumType", fetch = FetchType.LAZY)
    private SeatTemplate seatTemplate;

    public AuditoriumType(String code, String name, int rows, int cols) {
        this.code = code; this.name = name;
        this.seatTemplate = new SeatTemplate(this, rows, cols);
    }
}