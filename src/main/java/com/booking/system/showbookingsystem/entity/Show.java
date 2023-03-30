package com.booking.system.showbookingsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    public Long showNumber;
    @Column
    public int rows;
    @Column
    public int seatsPerRow;
    @Column
    public int cancellationTime;

    public Long getShowNumber() {
        return showNumber;
    }

    public int getRows() {
        return rows;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getCancellationTime() {
        return cancellationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return rows == show.rows && seatsPerRow == show.seatsPerRow && cancellationTime == show.cancellationTime && Objects.equals(showNumber, show.showNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showNumber, rows, seatsPerRow, cancellationTime);
    }

    @Override
    public String toString() {
        return "Show{" +
                "showNumber=" + showNumber +
                ", rows=" + rows +
                ", seatsPerRow=" + seatsPerRow +
                ", cancellationTime=" + cancellationTime +
                '}';
    }
}
