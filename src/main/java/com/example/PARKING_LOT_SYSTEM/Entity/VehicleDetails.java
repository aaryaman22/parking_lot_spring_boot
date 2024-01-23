package com.example.PARKING_LOT_SYSTEM.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "VEHICLE_DETAILS")
@Builder  // we can use .builder to set values in set function
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDetails {
    @Id
    @Column(name = "ID")
    private String regestrationNumber;
    @Column(name = "VEHICLE_TYPE")
    private String type;
    @Column(name = "SLOT_SR_NUMBER")
    private Integer slotSrNumber;
    @Column(name = "DATE")
    private LocalDateTime localDateTime;

    @OneToOne
    @JoinColumn(name = "SLOT_SR_NUMBER", insertable = false, updatable = false)
    private Slot slot;
}
