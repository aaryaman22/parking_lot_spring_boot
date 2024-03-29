package com.example.PARKING_LOT_SYSTEM.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.List;

@Entity
@Table(name = "PARKING_LEVELS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "level.slots" , attributeNodes = {@NamedAttributeNode("slotsList")})
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "OCCUPIED_BIKE_SPOTS")
    private int occupiedBikeSpot;

    @Column(name = "OCCUPIED_CAR_SPOTS")
    private int occupiedCarSpot;

    @Column(name = "OCCUPIED_BUS_SPOTS")
    private int occupiedBusSpot;

    @Column(name = "AVAILABLE_BIKE_SPOTS")
    private int availableBikeSpot;

    @Column(name = "AVAILABLE_CAR_SPOTS")
    private int availableCarSpot;

    @Column(name = "AVAILABLE_BUS_SPOTS")
    private int availableBusSpot;

    @OneToMany
    @JoinColumn(name = "LEVEL_ID" , referencedColumnName = "ID")
    private List<Slot> slotsList;
}
