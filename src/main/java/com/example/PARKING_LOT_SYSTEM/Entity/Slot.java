package com.example.PARKING_LOT_SYSTEM.Entity;

import com.example.PARKING_LOT_SYSTEM.Model.VehicleModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PARKING_SLOT")
@Builder  // we can use .builder to set values in set function
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "SR_NUMBER") DO WE HAVE TO MENTION IT /?
    private Integer id;
    @Column(name = "SLOT_NUMBER")
    private Integer slotNumber;
    @Column(name = "LEVEL_ID")
    private Integer levelId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "OCCUPIED")
    private Boolean occupied;

    @OneToOne(mappedBy = "slot")
    private VehicleDetails vehicleDetails;

}
