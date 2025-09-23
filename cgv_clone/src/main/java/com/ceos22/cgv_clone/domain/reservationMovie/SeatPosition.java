package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatPosition {

    @Column(name="row_no", nullable=false)
    private int rowNo;
    @Column(name="col_no", nullable=false)
    private int colNo;

    public SeatPosition(int rowNo, int colNo) {
        if (rowNo<=0 || colNo<=0) throw new IllegalArgumentException();
        this.rowNo=rowNo; this.colNo=colNo;
    }
}