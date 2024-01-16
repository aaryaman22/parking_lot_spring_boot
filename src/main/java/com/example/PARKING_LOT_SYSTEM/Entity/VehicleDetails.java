package com.example.PARKING_LOT_SYSTEM.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
    @Column(name = "LEVEL_ID")
    private int levelId;
    @Column(name = "SLOT_SR_NUMBER")
    private int slotSrNumber;
}
